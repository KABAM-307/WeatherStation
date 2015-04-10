
/* Library functions */

/*
 * THis function sets conditions to be able to read from MCP3008, using SPI
 * It needs to be called only once, and can be done at the start of the main function. 
 * If the return value is less than zero, terminate the program
 */
int init(void);

/*
 * This function reads analog input from the MCP3008.
 * The parameter 'int channel' specifies the channel number to read from, for example 0.
 * An ADC value between 0-1023 is then returned. 
 */
int readADC(int );

