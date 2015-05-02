CFLAGS = -std=c99 -lwiringPi

all: main dht22 light pressure wind

main: *.java
	javac Main.java

dht22: dht22.c
	gcc -o $@ $^ $(CFLAGS)

pressure: pressure.c
	gcc -o $@ $^ $(CFLAGS)

light: light.c
	gcc -o $@ $^ $(CFLAGS) -L. -ladc

wind: wind.c
	gcc -o $@ $^ $(CFLAGS) -L. -ladc

clean:
	rm dht22
	rm light
	rm pressure
	rm wind
	rm *.class
