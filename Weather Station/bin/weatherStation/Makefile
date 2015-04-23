CFLAGS = -std=c99 -lwiringPi

all: main dht22 light pressure

main: *.java
	javac Main.java

dht22: dht22.c
	gcc -o $@ $^ $(CFLAGS)

pressure: pressure.c
	gcc -o $@ $^ $(CFLAGS)

light: light.c
	gcc -o $@ $^ $(CFLAGS) -L. -ladc

clean:
	rm dht22
	rm light
	rm pressure
	rm *.class
