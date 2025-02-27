#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include <ESP32Servo.h>

Servo myservo;
//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "ECC"
#define WIFI_PASSWORD "12345678"

// Insert Firebase project API Key
#define API_KEY "AIzaSyA-WT9ViS5xlnE7gAf3iZlzeU3O7Jb7wWc"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://smartparking-bad24-default-rtdb.firebaseio.com/"

//Define Firebase Data object
FirebaseData stream;
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

String parentPath = "/FromPi";
String childPath[1] = {"/command"};
String str[1];
int count = 0;
bool signupOK = false;
unsigned long sendDataPrevMillis = 0, readDataPrevMillis = 0, sendDataPrevMillis2 = 0;
volatile bool dataChanged = false;
const int servoPin = 19;

void streamCallback(MultiPathStream stream)
{
  size_t numChild = sizeof(childPath) / sizeof(childPath[0]);
  for (size_t i = 0; i < numChild; i++)
  {
    if (stream.get(childPath[i]))
    {
      str[i] = stream.value.c_str();
      Serial.printf("path: %s, event: %s, type: %s, value: %s%s", stream.dataPath.c_str(), stream.eventType.c_str(), stream.type.c_str(), stream.value.c_str(), i < numChild - 1 ? "\n" : "");
    }
  }
  Serial.println();
  Serial.printf("Received stream payload size: %d (Max. %d)\n\n", stream.payloadLength(), stream.maxPayloadLength());
  dataChanged = true;
}

void streamTimeoutCallback(bool timeout)
{
  if (timeout)
    Serial.println("stream timed out, resuming...\n");

  if (!stream.httpConnected())
    Serial.printf("error code: %d, reason: %s\n\n", stream.httpCode(), stream.errorReason().c_str());
}


uint32_t pmillisA = 0, pmillisB = 0, pmillisC = 0;
void setup() {
  pinMode(2, OUTPUT);
  digitalWrite(2, LOW);


  Serial.begin(115200);
  Serial2.begin(9600);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("ok");
    signupOK = true;
  }
  else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; // see addons/TokenHelper.h
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  if (!Firebase.RTDB.beginMultiPathStream(&stream, parentPath))
    Serial.printf("sream begin error, %s\n\n", stream.errorReason().c_str());

  Firebase.RTDB.setMultiPathStreamCallback(&stream, streamCallback, streamTimeoutCallback);
  Serial.println("Wifi Connected !!");
  pmillisA = millis(); pmillisB = millis(); pmillisC = millis();
  digitalWrite(2, HIGH);
  //  // Allow allocation of all timers
    ESP32PWM::allocateTimer(0);
    ESP32PWM::allocateTimer(1);
    ESP32PWM::allocateTimer(2);
    ESP32PWM::allocateTimer(3);
    myservo.setPeriodHertz(50);    // standard 50 hz servo
    myservo.attach(servoPin, 1000, 2000);
    myservo.write(10);
    Serial.println("Success");
//    myservo.detach();
}

String slot1 = " ", slot2 = " ", slot3 = " ", slot4 = " ", slot5 = " ", slot6 = " ";
String fees1 = " ", fees2 = " ", fees3 = " ", datachange = " ";
int feeA = 0, feeB = 0, feeC = 0;
int unit = 1;
String feesA = " ", feesB = " ", feesC = " ";
uint32_t pmillis = 0;
String tempslot1 = " ", tempslot2 = " ", tempslot3 = " ", tempslot4 = " ", tempslot5 = " ", tempslot6 = " ";

void loop() {
  if (str[0] == "ALLOW") {
    Serial.println("Data Changed.");
    Serial.println(str[0]);
    myservo.write(120);
    delay(6000);
    myservo.write(10);    
    if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis2 > 3000 || sendDataPrevMillis2 == 0))
    {
      sendDataPrevMillis2 = millis();
      Firebase.RTDB.setString(&fbdo, "/FromPi/command", "0");
    }
  }
  if (Serial2.available() > 0)
  {
    String Data = Serial2.readStringUntil('&');
    Serial.println(Data);
    //    Serial.println(Data.length());
    if (Data.length() > 0 && Data.length() < 60)
    {
      slot1 = String_Split(Data, ';', 0);
      slot2 = String_Split(Data, ';', 1);
      slot3 = String_Split(Data, ';', 2);
      slot4 = String_Split(Data, ';', 3);
      slot5 = String_Split(Data, ';', 4);
      slot6 = String_Split(Data, ';', 5);
      fees1 = String_Split(Data, ';', 6);
      fees2 = String_Split(Data, ';', 7);
      fees3 = String_Split(Data, ';', 8);
      datachange = String_Split(Data, ';', 9);
    }
  }
  chargingFees();
  Firebase_Hit_Data();
}

// ************************************************************************************
void chargingFees() {
  if (millis() - pmillisA > 1000&& fees1 == "YES") {
    pmillisA = millis(); feeA += unit+5;
    feesA = (String)feeA;
    Serial.print("FeesA: ");
    Serial.println(feesA);
  }
  else if (fees1 == "NO") {
    feesA = "0"; feeA = -100;
  }
  if (millis() - pmillisB > 1000 && fees2 == "YES") {
    pmillisB = millis(); feeB += unit+5;
    feesB = (String)feeB;
    Serial.print("  FeesB: ");
    Serial.println(feesB);
  }
  else if (fees2 == "NO") {
    feesB = "0"; feeB = -100;
  }
  if (millis() - pmillisC > 1000 && fees3 == "YES") {
    pmillisC = millis(); feeC += unit+5;
    feesC = (String)feeC;
    Serial.print("  FeesC: ");
    Serial.println(feesC);
  }
  else if (fees3 == "NO") {
    feesC = "0"; feeC = -100;
  }
}

String tempfees1 = " ", tempfees2 = " ", tempfees3 = " ";
void Firebase_Hit_Data()
{
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 3000 || sendDataPrevMillis == 0))
  {
    sendDataPrevMillis = millis();
    bool datac = false;
    if (slot1 != tempslot1) {
      tempslot1 = slot1;
      datac = true;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/Authorize/1/status", slot1);
      //      Serial.println("1");
    }
    if (slot2 != tempslot2) {
      tempslot2 = slot2;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/Authorize/2/status", slot2);
      datac = true;
      //      Serial.println("2");
    }
    if (slot3 != tempslot3) {
      tempslot3 = slot3;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/Authorize/3/status", slot3);
      datac = true;

      //      Serial.println("3");
    }
    if (slot4 != tempslot4) {
      tempslot4 = slot4;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/UnAuthorize/1/status", slot4);
      datac = true;
      //      Serial.println("4");
    }
    if (slot5 != tempslot5) {
      tempslot5 = slot5;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/UnAuthorize/2/status", slot5);
      datac = true;
      //      Serial.println("5");
    }
    if (slot6 != tempslot6) {
      tempslot6 = slot6;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/UnAuthorize/3/status", slot6);
      datac = true;
      //      Serial.println("6");
    }
    if (feesA != tempfees1) {
      tempfees1 = feesA;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/UnAuthorize/1/fees", feesA);
      datac = true;
    }
    if (feesB != tempfees2) {
      tempfees2 = feesB;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/UnAuthorize/2/fees", feesB);
      datac = true;
    }
    if (feesC != tempfees3) {
      tempfees3 = feesC;
      Firebase.RTDB.setString(&fbdo, "/ParkingSlots/UnAuthorize/3/fees", feesC);
      datac = true;
    }
    Serial.println("Data Sent.");
  }
}

String String_Split(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = { 0, -1 };
  int maxIndex = data.length();

  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
