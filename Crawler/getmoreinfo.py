import csv
import pandas as pd
from selenium import webdriver
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By

#Browser is headless
options = Options()
options.headless = True

with open("rensetkurser.csv", "r") as csvfile:
	reader = csv.reader(csvfile, delimiter="@")
	next(csvfile)
	with open("kursermedinfo.csv","w", encoding="utf8",newline="") as newfile:
		writer = csv.writer(newfile, delimiter=";")
		writer.writerow(["ID","Title","Language","ECTS","Year","Placement", "Course Type","prerequisites", "Not appl. with"])
		for row in reader:
			driver = webdriver.Firefox(options=options)
			url = r"http://kurser.dtu.dk/course/%s" % (row[0])
			driver.get(url)

			try:
				print(row[0])
				myElem = WebDriverWait(driver, 3).until(EC.presence_of_element_located((By.ID, "pagecontents")))
				html = driver.page_source
				tables = pd.read_html(html)
				driver.close()
				df = tables[1]
				df.columns = ["One","Two"]
				not_appl_df = df[df['One'].str.contains("applicable", na=False)]
				prereq_df = df[df['One'].str.contains("prerequisites", na=False)]
				not_appl = ""
				prereq = ""
				if not_appl_df.empty:
					not_appl = "NA"
				else:
					not_appl = not_appl_df.iloc[0]['Two']

				if prereq_df.empty:
					prereq = "NA"
				else:
					prereq = prereq_df.iloc[0]["Two"]
				row.extend([prereq,not_appl])
				writer.writerow(row)

			except TimeoutException:
				print("Loading took too much time!")