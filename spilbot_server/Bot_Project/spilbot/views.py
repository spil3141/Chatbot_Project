from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.decorators import login_required
# from cache.shortcuts import permission_required
# Create your views here.

def get_request(request):
    if request.method == "GET":
        msg = request.GET["q"]
        
    return HttpResponse(msg)






# def Index(request):
#     if request.method == "GET":
#         print(request.GET["q"])
#     return render(request,"spilbot/html/Index.html")
