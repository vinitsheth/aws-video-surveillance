import threading
import urllib2
import time

start = time.time()
urls = ["http://13.57.38.214:8080/RunScript"]*1

def fetch_url(url):
    urlHandler = urllib2.urlopen(url)
    html = urlHandler.read()
    print "'%s\' fetched in %ss" % (url, (time.time() - start))

threads = [threading.Thread(target=fetch_url, args=(url,)) for url in urls]
for thread in threads:
    thread.start()
for thread in threads:
    thread.join()

print "Elapsed Time: %s" % (time.time() - start)