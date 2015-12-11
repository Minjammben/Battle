CLASSPATH = .;lib/*

APPNAME = app.jar

#OPSYS = linux
OPSYS = windows 
DLLDIR= lib/native/$(OPSYS)

JFLAGS = -d ./ -cp $(CLASSPATH)
JC = javac

CLASSES = \
graphics/Graphics.java \
world/WorldInterface.java \
world/Actor.java \
world/Character.java \
world/Action.java \
world/StatusEffect.java \
world/Player.java \
world/World.java \
game/State.java \
game/TutorialState.java \
game/WorldState.java \
game/GameInterface.java \
game/Game.java \
Main.java \

#probably don't need this but keep it for legacy reasons
.SUFFIXES: .java .class
#.java.class: 
#	echo "$(JC) $(JFLAGS) $*.java" | cmd.exe
#classes: $(CLASSES:.java=.class)

default: allclasses jar

run: allclasses jar exec

allclasses:
	echo "$(JC) $(JFLAGS) $(CLASSES)" | cmd.exe

exec: 
	echo " "; echo "java -cp $(CLASSPATH);app.jar -Djava.library.path=$(DLLDIR) Main" | cmd.exe

jar: 
	jar -cmf manifest.mf $(APPNAME) ./*.class

clean:
	$(RM) *.class $(APPNAME)