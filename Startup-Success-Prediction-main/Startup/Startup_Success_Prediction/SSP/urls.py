from django.urls import path

from . import views
from .views import UpdatePostView

app_name = "SSP"

urlpatterns = [
    path('homepage/', views.homepage, name="homepage"),
    path('profile/', views.profile, name="profile"),
    path('signup/', views.signup, name="signup"),
    path('', views.loginPage, name="loginPage"),
    path("logout", views.logout_request, name="logout"),

    path("add_blogs/", views.add_blogs, name="addBlog"),
    path("blogs/", views.blogs, name="blogs"),
    path("blog/<str:slug>/", views.blogs_comments, name="blogs_comments"),
    path("add_blogs/", views.add_blogs, name="add_blogs"),
    path("edit_blog_post/<str:slug>/",
         UpdatePostView.as_view(),  name="edit_blog_post"),
    path("delete_blog_post/<str:slug>/",
         views.Delete_Blog_Post, name="delete_blog_post"),


]
