from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.decorators import login_required
# from cache.shortcuts import permission_required
# Create your views here.

def Index(request):
    return render(request,"spilbot/html/Index.html")

# @login_required
# @permission_required('documents.add_documentindex', raise_exception=True)
def savelog(request):
    import pydevd;pydevd.settrace()
    log_text = request.POST['log']
    with open("scanning.log", "a") as f:
        f.write(log_text)
    return HttpResponse('ok')
