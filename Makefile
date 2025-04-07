run:
	export $(cat .env | xargs)

docs:
	pdflatex  -output-directory=doc/ doc/ManualeUtente.tex


# Get-Content .env | ForEach-Object {
    # $name, $value = $_ -split '='
    # [System.Environment]::SetEnvironmentVariable($name, $value, [System.EnvironmentVariableTarget]::Process)
# }
