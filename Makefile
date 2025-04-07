run:
	export $(cat .env | xargs)

docs:
	pdflatex  -output-directory=doc/ doc/ManualeUtente.tex