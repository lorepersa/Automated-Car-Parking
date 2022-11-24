#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import argparse

GPIO.setmode(GPIO.BOARD)

parser = argparse.ArgumentParser(description='Control a HC-SR04 Sonar.')
parser.add_argument('--trigger', type=int, default=11, help='Trigger pin number. Note: GPIO Board number.')
parser.add_argument('--echo', type=int, default=13, help='Echo pin number. Note: GPIO Board number.')

args = parser.parse_args()


def setup(pinTrigger: int, pinEcho: int):
    GPIO.setwarnings(False)
    GPIO.setup(pinTrigger, GPIO.OUT)
    GPIO.setup(pinEcho, GPIO.IN)
    GPIO.output(pinTrigger, GPIO.LOW)


def distance(pinTrigger: int, pinEcho: int) -> int:
    pulse_start_time = 0.0
    pulse_end_time = 0.0
    GPIO.output(pinTrigger, GPIO.HIGH)
    time.sleep(0.00001)
    GPIO.output(pinTrigger, GPIO.LOW)
    while GPIO.input(pinEcho) == 0:
        pulse_start_time = time.time()
    while GPIO.input(pinEcho) == 1:
        pulse_end_time = time.time()
    pulse_duration = pulse_end_time - pulse_start_time
    distance = int(round(pulse_duration * 17150, 2))

    return distance


def main():
    setup(pinTrigger=args.trigger, pinEcho=args.echo)
    d = distance(pinTrigger=args.trigger, pinEcho=args.echo)
    print(d)


if __name__ == '__main__':
    main()
