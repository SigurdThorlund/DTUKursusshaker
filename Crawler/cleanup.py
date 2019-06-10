# -- coding: utf-8 -- 
import csv

with open("kurser_raw.csv", "r",encoding="utf8") as csvfile:
	reader = csv.reader(csvfile, delimiter="@")
	with open("rensetkurser.csv","w",newline="") as newfile:
		writer = csv.writer(newfile, delimiter="@")
		writer.writerow(["ID","Title","Language","ECTS","Year","Placement", "Course Type"])
		for row in reader:
			if len(row) == 1:
				continue

			i = 0
			ID = ""
			title = ""
			lang = ""
			ects = ""
			year = ""
			placement = ""
			alt_placement = ""
			course_type = ""

			for x in row:
				if x == "":
					continue
				x = x.replace("\n", "")
				x = x.replace(r"\n", "")
				if i % 2 == 0:
					ID = x.split("-")[0][:-1]
					title= x[x.find("-")+2:x.find("Dansk")-1 if x.find("Dansk") != -1 else x.find("Engelsk")-1]
					lang = "Dansk" if x.find("Dansk") != -1 else "Engelsk"
					ects = x[x.find("|")+2:x.find("ECTS")-1]
					year = x.split("|")[2][1:-1]
					placement = x.split("|")[3][1:]
				else:
					course_type = x
				i+=1

			writer.writerow([ID,title,lang,ects,year,placement,course_type])