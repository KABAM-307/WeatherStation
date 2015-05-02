#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <wiringPi.h>
#include <wiringPiSPI.h>

#include "adc.h"

int main(int argc, char **argv){

	if (wiringPiSetup() == -1)
		return 1;
	
	/*WiringPi SPI Setup - for MCP3008*/
	if(init() < 0)
		return 1;

  //Read channel
	int chan = 0;

  //Set to first param
	if(argc > 1)
		chan = atoi(argv[1]);

  int avg = 0;
  for (int i = 0; i < 20; i++) {
    int reading = readADC(chan);
    reading *= 100;
    reading /= 1023;

    avg += reading;

    delay(50);
  }

  //Max is only 2V, when we're reading at 3.3V
  avg = (int) avg*(2/3.3);

  printf("%0.2f\n", (double) avg/20);

	return 0;
}
