exportenv:
	sh loadEnv.sh

run:
	make exportenv
	./gradlew bootRun

