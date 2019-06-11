import re
rdd = sc.wholeTextFiles("path to your csv file")\
    .map(lambda x: re.sub(r'(?!(([^"]*"){2})*[^"]*$),', ' ', x[1].replace("\r\n", ",").replace(",,", ",")).split(","))\
    .flatMap(lambda x: [x[k:k+8] for k in range(0, len(x), 8)])