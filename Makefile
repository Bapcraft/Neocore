BUILD_OUTPUT_DIR=target
JZ_DIR=jzania

build: clean
	mvn clean install
	mkdir -p ${BUILD_OUTPUT_DIR}
	find . -name '*.jar' -exec cp {} ${BUILD_OUTPUT_DIR} \;
	find ${BUILD_OUTPUT_DIR} -name 'original-*' -exec rm {} \;

clean:
	mvn clean > /dev/null
	rm -rf ${BUILD_OUTPUT_DIR}

deps:
	git clone https://github.com/treyzania/jzania.git ${JZ_DIR}
	mvn -f ${JZ_DIR}/pom.xml clean install
	rm -rf ${JZ_DIR}
