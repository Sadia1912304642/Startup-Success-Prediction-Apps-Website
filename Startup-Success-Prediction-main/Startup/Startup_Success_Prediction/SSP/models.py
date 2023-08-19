from django.db import models
from django.contrib.auth.models import User
from django.urls import reverse
from django.utils.timezone import now
# Create your models here.


class Startup(models.Model):

    name = models.CharField(max_length=255)
    start = models.PositiveIntegerField(default=0)

    categories = (
        ('Software', "Software"),
        ('Web', 'Web'),
        ('Mobile', 'Mobile'),
        ('Enterprise', 'Enterprise'),
        ('Advertising', 'Advertising'),
        ('Gaming Co.', 'Gaming Co.'),
        ('E-ommerce', 'E-commerce'),
        ('Bio-tech', 'Bio-tech'),
        ('Consulting', 'Consulting'),
        ('Others', 'Others'),
    )
    category = models.CharField(max_length=100, choices=categories)

    relationship_num = models.PositiveIntegerField(default=0)
    avg_participants = models.PositiveIntegerField(default=0)
    milestone_num = models.PositiveIntegerField(default=0)
    first_milestone = models.PositiveIntegerField(default=0)
    last_milestone = models.PositiveIntegerField(default=0)

    total_fund = models.PositiveIntegerField()
    first_funding = models.PositiveIntegerField(default=0)
    last_funding = models.PositiveIntegerField(default=0)
    funding_round_num = models.PositiveIntegerField(default=0)
    has_angel = models.BooleanField(default=False)
    has_vc = models.BooleanField(default=False)

    has_A = models.BooleanField(default=False)
    has_B = models.BooleanField(default=False)
    has_C = models.BooleanField(default=False)
    has_D = models.BooleanField(default=False)

    class Meta:
        db_table = 'Startup'

    def __str__(self):
        return self.name


class BlogPost(models.Model):
    title = models.CharField(max_length=255)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    slug = models.CharField(max_length=130)
    content = models.TextField()
    image = models.ImageField(upload_to="profile_pics", blank=True, null=True)
    dateTime = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return str(self.author) + " Blog Title: " + self.title

    def get_absolute_url(self):
        return reverse('SSP:blogs')


class Comment(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    content = models.TextField()
    blog = models.ForeignKey(BlogPost, on_delete=models.CASCADE)
    parent_comment = models.ForeignKey(
        'self', on_delete=models.CASCADE, null=True, blank=True)
    dateTime = models.DateTimeField(default=now)

    def __str__(self):
        return self.user.username + " Comment: " + self.content
