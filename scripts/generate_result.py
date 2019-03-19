#import subprocess
from subprocess import Popen, PIPE
from os.path import basename
import os
from urlparse import urlsplit
import urllib2
import time
def url2name(url):
    return basename(urlsplit(url)[2])

def download(url, out_path):
    localName = url2name(url)
    req = urllib2.Request(url)
    r = urllib2.urlopen(req)
    if r.info().has_key('Content-Disposition'):
        
        localName = r.info()['Content-Disposition'].split('filename=')[1]
        if localName[0] == '"' or localName[0] == "'":
            localName = localName[1:-1]
    elif r.url != url: 
        
        localName = url2name(r.url)
    
    localName = os.path.join(out_path, localName)
    fname = localName
    f = open(localName, 'wb')
    f.write(r.read())
    f.close()
    return fname



def process(video_path):
    encoding = 'latin1'
    cmds = ['cd /home/ubuntu/darknet',
    'Xvfb :1 & export DISPLAY=:1',
    './darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg  tiny.weights '+ video_path +' -dont_show > result',
    ' python darknet_test.py '
    ]
    commands = 'cd /home/ubuntu/darknet \n Xvfb :1 & export DISPLAY=:1 \n ./darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg  tiny.weights '+ video_path +' -dont_show > result \n python darknet_test.py \n'
    # p = subprocess.Popen('/bin/bash', stdin=subprocess.PIPE,
    #             stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # for cmd in cmds:
    #     p.stdin.write(cmd + "\n")
    # p.stdin.close()
    process = Popen( "/bin/bash", shell=False, universal_newlines=True,
                  stdin=PIPE, stdout=PIPE, stderr=PIPE )                             
    out, err = process.communicate( commands ) 
    with open('result_label') as f:
        content = f.readlines()
    os.remove(video_path)
    os.remove('result_label')
    print (video_path,content,time.time()-start)
    #print (out,err)
start = time.time()   
video_path = download("http://206.207.50.7/getvideo", 'video')
process(video_path)