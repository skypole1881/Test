export CASINO=$(CURDIR)

.phony: all doc run clean

SRCDIR = src
BINDIR = bin
DOCDIR = doc

all:
	mkdir $(BINDIR) ||  javac -d ./bin/ ./src/*.java -cp ./lib/
	javadoc -d doc ./src/*.java -cp ./lib/
run:
	java -cp lib/:bin/ foop.Casino 20 1000 PlayerB00201011 PlayerB03204030 PlayerB00201011 PlayerB03204030
clean:
	rm -rf *.class
	rm -rf doc/
