import json
from tracemalloc import start
from unicodedata import category
from django.shortcuts import render
from .forms import *
# Create your views here.
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.contrib.auth import login, authenticate
from django.contrib.auth.forms import AuthenticationForm
from django.contrib import messages

from django.contrib.auth import login, authenticate, logout  # add this
from django.contrib import messages
from django.contrib.auth.forms import AuthenticationForm
from django.views.generic import View
from jinja2 import Undefined  # add this
from .forms import *
from .models import *
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.views.generic.edit import CreateView, UpdateView
from django.views.generic import TemplateView

# importing the requests library
import requests

# defining the api-endpoint
API_ENDPOINT = "http://127.0.0.1:8000/api/predict"


def homepage(request):
    form = StartupModelForm(request)
    show_result = "Fill up form to get prediction"
    if request.method == 'GET':
        form = StartupModelForm(request.GET, request)
        if form.is_valid():
            name = form.cleaned_data.get("name")
            age = form.cleaned_data.get("start")
            catagory = form.cleaned_data.get("category")
            relationship = form.cleaned_data.get("relationship_num")
            participants = form.cleaned_data.get("avg_participants")
            milestone = form.cleaned_data.get("milestone_num")
            first_milestone = form.cleaned_data.get("first_milestone")
            last_milestone = form.cleaned_data.get("last_milestone")
            total_fund = form.cleaned_data.get("total_fund")
            first_funding = form.cleaned_data.get("first_funding")
            last_funding = form.cleaned_data.get("last_funding")
            funding_round_num = form.cleaned_data.get("funding_round_num")
            has_angel = form.cleaned_data.get("has_angel")
            has_vc = form.cleaned_data.get("has_vc")
            has_A = form.cleaned_data.get("has_A")
            has_B = form.cleaned_data.get("has_B")
            has_C = form.cleaned_data.get("has_C")
            has_D = form.cleaned_data.get("has_D")

            is_software = 0
            is_web = 0
            is_advertise = 0
            is_biotech = 0
            is_consulting = 0
            is_ecommerce = 0
            is_enterprise = 0
            is_gaming = 0
            is_mobile = 0
            is_others = 0

            if category == 'Software':
                is_software = 1
            elif category == 'Web':
                is_web = 1
            elif category == 'Mobile':
                is_mobile = 1
            elif category == 'Enterprise':
                is_enterprise = 1
            elif category == 'Advertising':
                is_advertise = 1
            elif category == 'Gaming Co.':
                is_gaming = 1
            elif category == 'E-ommerce':
                is_ecommerce = 1
            elif category == 'Bio-tech':
                is_biotech = 1
            elif category == 'Consulting':
                is_consulting = 1
            elif category == 'Others':
                is_others = 1

            if has_A or has_B or has_C or has_D:
                has_ABCD = 1
            else:
                has_ABCD = 0

            if has_vc or has_angel:
                has_investor = 1
            else:
                has_investor = 0

            if has_ABCD and has_investor:
                has_seed = 1
            else:
                has_seed = 0

            if has_ABCD and has_vc and has_angel:
                invalid_startup = 0
            else:
                invalid_startup = 1

            data = {
                "age_first_funding_year": first_funding,
                "age_last_funding_year": last_funding,
                "age_first_milestone_year": first_milestone,
                "age_last_milestone_year": last_milestone,
                "relationships": relationship,
                "funding_rounds": funding_round_num,
                "funding_total_usd": total_fund,
                "milestones": milestone,
                "is_software": is_software,
                "is_web": is_web,
                "is_mobile": is_mobile,
                "is_enterprise": is_enterprise,
                "is_advertising": is_advertise,
                "is_gamesvideo": is_gaming,
                "is_ecommerce": is_ecommerce,
                "is_biotech": is_biotech,
                "is_consulting": is_consulting,
                "is_othercategory": is_others,
                "avg_participants": participants,
                "has_RoundABCD": has_ABCD,
                "has_Investor": has_investor,
                "has_Seed": has_seed,
                "invalid_startup": invalid_startup,
                "startUp_age_year": age,
            }

            r = requests.post(url=API_ENDPOINT, data=data)

            json_response = r.text

            result = json.loads(json_response)

            if result["Startup_status"] == 1:
                show_result = name + " will be Successful."
            else:
                show_result = name + " have to work hard!"

            print("The pastebin URL is:" + str(result["Startup_status"]))
        return render(request, 'index.html', {'form': form, 'show_result': show_result})
    return render(request, 'index.html', {'form': form, 'show_result': show_result})


def signup(request):
    form = SignUpForm(request.POST)
    if form.is_valid():
        form.save()
        username = form.cleaned_data.get('username')
        password = form.cleaned_data.get('password')
        user = authenticate(username=username, password=password)
        # login(request, user)
        return redirect('SSP:loginPage')
    context = {
        'form': form
    }
    return render(request, 'register.html', context)


def loginPage(request):
    if request.user.is_authenticated:
        return redirect('SSP:homepage')
    else:
        return render(request, 'login.html')
    #     if request.method == "POST":
    #         form = AuthenticationForm(request, data=request.POST)
    #         if form.is_valid():
    #             username = form.cleaned_data.get('username')
    #             password = form.cleaned_data.get('password')
    #             user = authenticate(username=username, password=password)
    #             if user is not None:
    #                 login(request, user)
    #                 messages.info(
    #                     request, f"You are now logged in as {username}.")
    #                 return redirect("SSP:homepage")
    #             else:
    #                 messages.error(request, "Invalid username or password.")
    #         else:
    #             messages.error(request, "Invalid username or password.")
    # form = AuthenticationForm()
    # return render(request=request, template_name="login.html", context={"login_form": form})


def logout_request(request):
    logout(request)
    messages.info(request, "You have successfully logged out.")
    return redirect("SSP:loginPage")


def profile(request):

    return render(request, 'profile.html')


def blogs(request):
    posts = BlogPost.objects.all()
    posts = BlogPost.objects.filter().order_by('-dateTime')
    return render(request, "blog.html", {'posts': posts})


def add_blogs(request):
    if request.method == "POST":
        form = BlogPostForm(data=request.POST, files=request.FILES)
        if form.is_valid():
            blogpost = form.save(commit=False)
            blogpost.author = request.user
            blogpost.save()
            obj = form.instance
            alert = True
            return render(request, "add_blogs.html", {'obj': obj, 'alert': alert})
    else:
        form = BlogPostForm()
    return render(request, "add_blogs.html", {'form': form})


def blogs_comments(request, slug):
    post = BlogPost.objects.filter(slug=slug).first()
    comments = Comment.objects.filter(blog=post)
    if request.method == "POST":
        user = request.user
        content = request.POST.get('content', '')
        blog_id = request.POST.get('blog_id', '')
        comment = Comment(user=user, content=content, blog=post)
        comment.save()
    return render(request, "blog_comments.html", {'post': post, 'comments': comments})


def Delete_Blog_Post(request, slug):
    posts = BlogPost.objects.get(slug=slug)
    if request.method == "POST":
        posts.delete()
        return redirect('/')
    return render(request, 'delete_blog_post.html', {'posts': posts})


class UpdatePostView(UpdateView):
    model = BlogPost
    template_name = 'edit_blog_post.html'
    fields = ['title', 'slug', 'content', 'image']
