GRADLE ?= gradle
MODS_DIR ?= $(HOME)/.minecraft/mods

.PHONY: all compile build test lint install clean

all: build

compile build:
	$(GRADLE) build

test:
	$(GRADLE) test

lint:
	$(GRADLE) check

install: build
	install -d $(MODS_DIR)
	cp build/libs/*.jar $(MODS_DIR)/

clean:
	$(GRADLE) clean

