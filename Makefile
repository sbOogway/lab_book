.PHONY: server client docs

run:
	export $(cat .env | xargs)

docs:
	# unsetopt correct_all
	pdflatex  -output-directory=doc/ doc/ManualeUtente.tex
	pdflatex  -output-directory=doc/ doc/ManualeTecnico.tex


server:
	java -jar server/target/server-1.0-jar-with-dependencies.jar

client:
	java -jar client/target/client-1.0-jar-with-dependencies.jar

# Get-Content .env | ForEach-Object {
    # $name, $value = $_ -split '='
    # [System.Environment]::SetEnvironmentVariable($name, $value, [System.EnvironmentVariableTarget]::Process)
# }
