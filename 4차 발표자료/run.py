import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import RPi.GPIO as GPIO
import time

motor_a = 5
motor_b = 6
GPIO.setmode(GPIO.BCM)
GPIO.setup(motor_a, GPIO.OUT)
GPIO.setup(motor_b, GPIO.OUT)

def close_door():
    GPIO.output(motor_a, GPIO.HIGH)
    GPIO.output(motor_b, GPIO.LOW)
    time.sleep(2)
    GPIO.output(motor_a, GPIO.LOW)
    GPIO.output(motor_b, GPIO.LOW)

def open_door():
    GPIO.output(motor_a, GPIO.LOW)
    GPIO.output(motor_b, GPIO.HIGH)
    time.sleep(2)
    GPIO.output(motor_a, GPIO.LOW)
    GPIO.output(motor_b, GPIO.LOW)


cred = credentials.Certificate('/home/pi/Desktop/reserveroom-61b03-firebase-adminsdk-jdj5w-b423f1df17.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://reservationmody-default-rtdb.firebaseio.com/'
})

before = 0
ref2 = db.reference()
ref = db.reference('Lock').child("401").child("lock")
before = ref.get()

while True:
    print(ref.get())
    if before != ref.get():
        before = ref.get()
        if ref.get() in "0":
            print("close")
            close_door()
        else :
            print("open")
            open_door()
    time.sleep(1)
