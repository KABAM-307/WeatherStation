#include <wiringPi.h>
#include <wiringPiI2C.h>

#include <fcntl.h>
#include <stdint.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
 
float getPressure(int i2c) {

 int i, n;

 unsigned char coef_request[3] = {3, 4, 8};

 unsigned char conv_request[3] = {0, 0x12, 0};

 float baro;
  
 int16_t a0coeff;
 int16_t b1coeff;
 int16_t b2coeff;
 int16_t c12coeff;

 float a0;
 float b1;
 float b2;
 float c12;
 
 int pressure, temp;
 float pressureComp;


 a0coeff = (( (uint16_t) wiringPiI2CReadReg8(i2c,4) << 8) | wiringPiI2CReadReg8(i2c,5));
 b1coeff = (( (uint16_t) wiringPiI2CReadReg8(i2c,6) << 8) | wiringPiI2CReadReg8(i2c,7));
 b2coeff = (( (uint16_t) wiringPiI2CReadReg8(i2c,8) << 8) | wiringPiI2CReadReg8(i2c,9));
 c12coeff = (( (uint16_t) (wiringPiI2CReadReg8(i2c,10) << 8) | wiringPiI2CReadReg8(i2c,11))) >> 2;

 a0 = (float)a0coeff / 8;
 b1 = (float)b1coeff / 8192;
 b2 = (float)b2coeff / 16384;
 c12 = (float)c12coeff;
 c12 /= 4194304.0;
  
 wiringPiI2CWriteReg8(i2c, 0x12, 0);
 delay(5);
  
 pressure = (( (uint16_t) wiringPiI2CReadReg8(i2c,0) << 8) | wiringPiI2CReadReg8(i2c,1)) >> 6;
 temp = (( (uint16_t) wiringPiI2CReadReg8(i2c,2) << 8) | wiringPiI2CReadReg8(i2c,3)) >> 6;
 
 pressureComp = a0 + (b1 + c12 * temp ) * pressure + b2 * temp;
 
 baro = ((65.0F / 1023.0F) * pressureComp) + 50.0F;

 baro *= 10;

 return baro;

}

int main(int argc, char* argv) {

 int i2c = wiringPiI2CSetup(0x60);  // 0x60 is bus address of mpl115a2

 if (i2c == -1) {
  printf("wiringPiI2CSetup failed\n");
  return 1;
 }

 printf("%0.2f\n", getPressure(i2c));
}
