#include <wiringPi.h>

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>

#define MAXTIMINGS 85

int data[5] = {0,0,0,0,0};

int readData(int pin, int* temp, int* humidity) {
    uint8_t state = HIGH;
    uint8_t counter = 0;
    int j = 0;

    data[0] = data[1] = data[2] = data[3] = data[4] = 0;

    //Pull it low first
    pinMode(pin, OUTPUT);
    digitalWrite(pin, LOW);
    delay(18);

    //Pull it high to tell it we want to read
    digitalWrite(pin, HIGH);
    delayMicroseconds(20);

    //Put pin in read mode
    pinMode(pin, INPUT);

    //Read data for MAXTIMINGS (datasheet says 85)
    for (int i = 0; i < MAXTIMINGS; i++) {
        counter = 0;
        while (digitalRead(pin) == state) {
            counter++;
            delayMicroseconds(1);
            if (counter == 255) break;
        }

        state = digitalRead(pin);

        if (counter == 255) break;

        if ((i >= 4) && (i%2 == 0)) {
            data[j/8] <<= 1;
            if (counter > 16)
                data[j/8] |= 1;
            j++;
        }
    }

    if ((j >= 40) && (data[4] == ((data[0] + data[1] + data[2] + data[3]) & 0xFF)) ) {
      *humidity = data[0] * 256 + data[1];
      *temp = (data[2] & 0x7F)* 256 + data[3];

      if ((data[2] & 0x80) != 0)
			  *temp *= -1;

		  return 1;
    }

    return 0;
}

int main(int argc, char* argv[]) {
  int pin = atoi(argv[2]);

  if (wiringPiSetup() == -1) {
    printf("WiringPi Failed to init");
    return -1;
  }

  int humidity = -1;
  int temp = -1;

  delay(2000);
  if (readData(pin, &temp, &humidity) > 0) {
    if (!strcmp(argv[1], "h")) {
      printf("%.2f\n", (float)(humidity/10.0));
    } else {
      printf("%.2f\n",  (float)(temp/10.0) * 1.8 + 32.0);
    } 
  }

  return 0;
}
