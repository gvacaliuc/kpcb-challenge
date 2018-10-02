SRC = src/**/*.java
PDF_FILE = ./gabe-vacaliuc-design-document.pdf
GRADLE_FILES = ./gradle/**/* ./gradlew ./gradlew.bat settings.gradle build.gradle
ZIPFILE = ./gabe-vacaliuc-kpcb-application.zip

zipfile:
	zip -r $(ZIPFILE) \
		. \
		-i $(SRC) \
		-i $(GRADLE_FILES) \
		-i $(PDF_FILE)

clean:
	$(RM) $(ZIPFILE)
