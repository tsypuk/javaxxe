DOCKER_COMPOSE_FILE = docker-compose.yaml

JDK7 = jdk7
JDK8 = jdk8

JRE7 = jre7
JRE8 = jre8

CONTAINER_NAME7 = ${JDK7}-build
CONTAINER_NAME8 = ${JDK8}-build

SERVICE_NAME = simpson-service
ORIGIN_JAR_FILE = java-xxe-0.1.0.jar
SERVICE_JAR_FILE = java-xxe

DOCKER_FILE7 = Dockerfile.${JRE7}.service
DOCKER_FILE8 = Dockerfile.${JRE8}.service

VERSION = 1

.PHONY: all build base_build package push clean

all: run7 run8

build_jdk8:
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build ${JDK8} \

build_app_jdk8: build_jdk8
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build ${JDK8}-build; \
	docker cp ${JDK8}-build:/home/gradle/service/simpsons-xxe/build/libs/$(ORIGIN_JAR_FILE) $(SERVICE_JAR_FILE).${JDK8}.jar

package8: build_app_jdk8
	docker build --file $(DOCKER_FILE8) --tag $(SERVICE_NAME).${JRE8}:$(VERSION) --tag $(SERVICE_NAME).${JRE8}:latest . \

run8: package8
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build $(SERVICE_NAME)-${JRE8} \

demo:
	docker-compose -f $(DOCKER_COMPOSE_FILE) up -d $(SERVICE_NAME)-${JRE8}; \
	docker-compose -f $(DOCKER_COMPOSE_FILE) up -d $(SERVICE_NAME)-${JRE7} \

unsecure:
	docker-compose -f $(DOCKER_COMPOSE_FILE) up -d --build $(SERVICE_NAME)-${JRE7}-unsecure; \

stop:
	docker-compose -f $(DOCKER_COMPOSE_FILE) stop \

# TODO refactor to parametrize
build_jdk7:
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build ${JDK7} \

build_app_jdk7: build_jdk7
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build ${JDK7}-build; \
	docker cp ${JDK7}-build:/home/gradle/service/simpsons-xxe/build/libs/$(ORIGIN_JAR_FILE) $(SERVICE_JAR_FILE).${JDK7}.jar

package7: build_app_jdk7
	docker build --file $(DOCKER_FILE7) --tag $(SERVICE_NAME).${JRE7}:$(VERSION) --tag $(SERVICE_NAME).${JRE7}:latest . \

run7: package7
	docker-compose -f $(DOCKER_COMPOSE_FILE) up --build $(SERVICE_NAME)-${JRE7} \
