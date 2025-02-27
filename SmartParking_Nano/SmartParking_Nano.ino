#include <SPI.h>
#include <MFRC522.h>
#include <LiquidCrystal.h>
#include <Servo.h>

Servo myservo;
#define RST_PIN 9
#define SS_PIN 10

byte readCard[4];
String MasterTag = "F091F01A";  // REPLACE this Tag ID with your Tag ID!!!
String tagID = "";

// Create instances
MFRC522 mfrc522(SS_PIN, RST_PIN);

// Connections
const int authorizeParkSensorA = A0;
const int authorizeParkSensorB = A4;
const int authorizeParkSensorC = 5;
const int UnauthorizeParkSensorA = A1;
const int UnauthorizeParkSensorB = A5;
const int UnauthorizeParkSensorC = 4;
const int LaserAuth_1 = A2;
const int LaserAuth_2 = 6;
const int LaserUnAuth_1 = A3;
const int LaserUnAuth_2 = 7;
const int buzzer = 8;
const int servopin = 3;

// Cloud Parameters
String slot1 = " ", slot2 = " ", slot3 = " ", slot4 = " ", slot5 = " ", slot6 = " ";
String fees1 = " ", fees2 = " ", fees3 = " ";

uint32_t pmillis = 0, pmillisA = 0, pmillisB = 0, pmillisC = 0;
int feesA = 0, feesB = 0, feesC = 0;
int unit = 100; // charging fees unit fees per min

void setup() {
  Serial.begin(9600);
  //  Serial.println("Initializing.........");
  pinMode(authorizeParkSensorA, INPUT);
  pinMode(authorizeParkSensorB, INPUT);
  pinMode(authorizeParkSensorC, INPUT);
  pinMode(UnauthorizeParkSensorA, INPUT);
  pinMode(UnauthorizeParkSensorB, INPUT);
  pinMode(UnauthorizeParkSensorC, INPUT);

  pinMode(LaserAuth_1, INPUT);
  pinMode(LaserAuth_2, INPUT);
  pinMode(LaserUnAuth_1, INPUT);
  pinMode(LaserUnAuth_2, INPUT);
  pinMode(2, INPUT);

  pinMode(buzzer, OUTPUT);
  digitalWrite(buzzer, HIGH);
  delay(250);
  digitalWrite(buzzer, LOW);

  // Initiating
  SPI.begin(); // SPI bus
  mfrc522.PCD_Init(); // MFRC522
  pmillis = millis(); pmillisA = millis(); pmillisB = millis(); pmillisC = millis();
  myservo.attach(servopin);
  myservo.write(10);
  delay(100);
  myservo.detach();
  delay(6000);
}

String tempslot1 = " ", tempslot2 = " ", tempslot3 = " ", tempslot4 = " ", tempslot5 = " ", tempslot6 = " ";
String tempfees1 = " ", tempfees2 = " ", tempfees3 = " ";
String datachange = " ";
void loop() {
  ScanLaser();
  ScanIR();
  getCard();
  //  chargingFees();
  //  toString();

  if (millis() - pmillis > 2500) {
    pmillis = millis();
    datachange = "NO";
    sendESP32();
  }
  //  debug();
  //  delay(300);
}

// **************************************************************************************************

bool parkA = false, parkB = false, parkC = false;
void ScanIR() {
  if (digitalRead(authorizeParkSensorA) == 0) {
    slot1 = "FULL";
  }
  else {
    slot1 = "EMPTY";
  }
  if (digitalRead(authorizeParkSensorB) == 0) {
    slot2 = "FULL";
  }
  else {
    slot2 = "EMPTY";
  }
  if (digitalRead(authorizeParkSensorC) == 0) {
    slot3 = "FULL";
  }
  else {
    slot3 = "EMPTY";
  }
  if (digitalRead(UnauthorizeParkSensorA) == 0) {
    slot4 = "FULL"; parkA = true; fees1 = "YES";
  }
  else {
    slot4 = "EMPTY"; parkA = false; fees1 = "NO";
  }
  if (digitalRead(UnauthorizeParkSensorB) == 0) {
    slot5 = "FULL"; parkB = true; fees2 = "YES";
  }
  else {
    slot5 = "EMPTY"; parkB = false; fees2 = "NO";
  }
  if (digitalRead(UnauthorizeParkSensorC) == 0) {
    slot6 = "FULL"; parkC = true; fees3 = "YES";
  }
  else {
    slot6 = "EMPTY"; parkC = false; fees3 = "NO";
  }
}

void ScanLaser() {
  if (digitalRead(LaserAuth_1) == 1 || digitalRead(LaserAuth_2) == 1 || digitalRead(LaserUnAuth_1) == 1 || digitalRead(LaserUnAuth_2) == 1) {
    digitalWrite(buzzer, HIGH);
  }
  else {
    digitalWrite(buzzer, LOW);
  }
}

void toString() {
  fees1 = (String)feesA;
  fees2 = (String)feesB;
  fees3 = (String)feesC;
}

void sendESP32() {
  Serial.print(slot1);
  Serial.print(";");
  Serial.print(slot2);
  Serial.print(";");
  Serial.print(slot3);
  Serial.print(";");
  Serial.print(slot4);
  Serial.print(";");
  Serial.print(slot5);
  Serial.print(";");
  Serial.print(slot6);
  Serial.print(";");
  Serial.print(fees1);
  Serial.print(";");
  Serial.print(fees2);
  Serial.print(";");
  Serial.print(fees3);
  Serial.print(";");
  Serial.print(datachange);
  Serial.println("&");
  delay(10);
}

void debug() {
  Serial.print(digitalRead(authorizeParkSensorA));
  Serial.print("  ");
  Serial.print(digitalRead(authorizeParkSensorB));
  Serial.print("  ");
  Serial.print(digitalRead(authorizeParkSensorC));
  Serial.print("  ");
  Serial.print(digitalRead(LaserAuth_1));
  Serial.print("  ");
  Serial.print(digitalRead(LaserAuth_2));
  Serial.print(" ********* ");
  Serial.print(digitalRead(UnauthorizeParkSensorA));
  Serial.print("  ");
  Serial.print(digitalRead(UnauthorizeParkSensorB));
  Serial.print("  ");
  Serial.print(digitalRead(UnauthorizeParkSensorC));
  Serial.print("  ");
  Serial.print(digitalRead(LaserUnAuth_1));
  Serial.print("  ");
  Serial.println(digitalRead(LaserUnAuth_2));
  Serial.println();
}


void barrierOpen() {
  for (int pos = 10; pos <= 120; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15 ms for the servo to reach the position
  }
}
void barrierClose() {
  for (int pos = 120; pos >= 10; pos -= 1) { // goes from 180 degrees to 0 degrees
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15 ms for the servo to reach the position
  }
}
void getCard() {
  //Wait until new tag is available
  if (getID())
  {
    if (tagID == MasterTag)
    {
      
      myservo.attach(servopin);
      myservo.write(100);
      delay(100);
      myservo.detach();
      delay(6000); // barrier delay
      myservo.attach(servopin);
      myservo.write(10);
      delay(100);
      myservo.detach();
    }
    else
    {
      // Serial.println("Access Denied");
    }
    Serial.println("ID : " + String(tagID));
//    delay(2000);
  }
}

boolean getID()
{
  // Getting ready for Reading PICCs
  if ( ! mfrc522.PICC_IsNewCardPresent()) { //If a new PICC placed to RFID reader continue
    return false;
  }
  if ( ! mfrc522.PICC_ReadCardSerial()) { //Since a PICC placed get Serial and continue
    return false;
  }
  tagID = "";
  for ( uint8_t i = 0; i < 4; i++) { // The MIFARE PICCs that we use have 4 byte UID
    //readCard[i] = mfrc522.uid.uidByte[i];
    tagID.concat(String(mfrc522.uid.uidByte[i], HEX)); // Adds the 4 bytes in a single String variable
  }
  tagID.toUpperCase();
  mfrc522.PICC_HaltA(); // Stop reading
  return true;
}
