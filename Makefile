.PHONY:

up:: injector-build consumer-build api-build linxo-build
	docker-compose up -d

injector:: injector-build
	docker-compose up -d injector

injector-build::
	cd demo-injector && ./mvnw clean package
	docker-compose build injector

consumer-build::
	cd demo-consumer && ./mvnw clean package
	docker-compose build consumer

consumer:: consumer-build
	docker-compose up -d consumer

api-build::
	cd demo-api && ./mvnw clean package
	docker-compose build api

api:: api-build
	docker-compose up -d api

linxo-build::
	cd demo-linxo-adapter && ./mvnw clean package
	docker-compose build linxo

linxo:: linxo-build
	docker-compose up -d linxo

pull-images::
	docker pull openjdk:8
	docker pull mongo:3.4
	docker pull confluentinc/cp-zookeeper:3.2.1
	docker pull confluentinc/cp-kafka:3.2.1
