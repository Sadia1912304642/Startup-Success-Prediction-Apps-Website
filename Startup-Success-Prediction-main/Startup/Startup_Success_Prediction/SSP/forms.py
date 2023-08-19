from attr import attr
from django import forms

from .models import *
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm


class StartupModelForm(forms.ModelForm):

    class Meta:
        model = Startup
        fields = '__all__'
        labels = {
            'name': 'Startup Name ',
            'start': 'Startup Age ',
            'category': 'Category ',
            'relationship_num': 'No. of Relationship ',
            'avg_participants': 'No. of Avg Participants',
            'milestone_num': 'No. of Milestone ',
            'first_milestone': 'First Milestone Age ',
            'last_milestone': "Last Milestone Age ",
            'total_fund': 'Total Funding (USD) ',
            'first_funding': 'First Funding Age ',
            'last_funding': 'Last Funding Age',
            'funding_round_num': 'No. of Funding Round ',
            'has_angel': "Angel Investor ",
            'has_vc': 'Venture Capital',
            'has_A': 'Series A ',
            'has_B': 'Series B ',
            'has_C': 'Series C ',
            'has_D': 'Series D ',

        }
        widgets = {
            'name': forms.TextInput(attrs={
                'class': 'form-control',
                'placeholder': 'Enter Startup name'
            }
            ),
            'start': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),

            'relationship_num': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'avg_participants': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'milestone_num': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 1,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'first_milestone': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'last_milestone': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'total_fund': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),

            'first_funding': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'last_funding': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'funding_round_num': forms.NumberInput(
                attrs={'step': 1,
                       "min_value": 0,
                       "max_value": 1000,
                       'class': 'form-control', 'placeholder': 'Enter the value', }),
            'has_angel': forms.CheckboxInput(attrs={'class': 'form-check-input', 'style': "padding : 9px"}),
            'has_vc': forms.CheckboxInput(attrs={'class': 'form-check-input', 'style': "padding : 9px"}),

            'has_A': forms.CheckboxInput(attrs={'class': 'form-check-input', 'style': "padding : 9px"}),
            'has_B': forms.CheckboxInput(attrs={'class': 'form-check-input', 'style': "padding : 9px"}),
            'has_C': forms.CheckboxInput(attrs={'class': 'form-check-input', 'style': "padding : 9px"}),
            'has_D': forms.CheckboxInput(attrs={'class': 'form-check-input', 'style': "padding : 9px"}),





        }


class SignUpForm(UserCreationForm):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['username'].widget.attrs.update({
            'class': 'form-input',
            'required': '',
            'name': 'username',
            'id': 'username',
            'type': 'text',
            'placeholder': 'Enter your name',
            'maxlength': '16',
            'minlength': '6',
        })
        self.fields['email'].widget.attrs.update({
            'class': 'form-input',
            'required': '',
            'name': 'email',
            'id': 'email',
            'type': 'email',
            'placeholder': 'Enter email',
        })
        self.fields['password1'].widget.attrs.update({
            'class': 'form-input',
            'required': '',
            'name': 'password1',
            'id': 'password1',
            'type': 'password',
            'placeholder': 'password',
            'maxlength': '22',
            'minlength': '8'
        })
        self.fields['password2'].widget.attrs.update({
            'class': 'form-input',
            'required': '',
            'name': 'password2',
            'id': 'password2',
            'type': 'password',
            'placeholder': 'Confirm password',
            'maxlength': '22',
            'minlength': '8'
        })

    username = forms.CharField(label='Username', max_length=20,)
    email = forms.EmailField(label='Email', max_length=100)
    password1 = forms.CharField(label='Password')
    password2 = forms.CharField(label='Confirm Password')

    class Meta:
        model = User
        fields = ('username', 'email', 'password1', 'password2', )


class BlogPostForm(forms.ModelForm):
    class Meta:
        model = BlogPost
        fields = ('title', 'slug', 'content', 'image')
        widgets = {
            'title': forms.TextInput(attrs={'class': 'form-control', 'placeholder': 'Title of the Blog'}),
            'slug': forms.TextInput(attrs={'class': 'form-control', 'placeholder': 'Copy the title with no space and a hyphen in between'}),
            'content': forms.Textarea(attrs={'class': 'form-control', 'placeholder': 'Content of the Blog'}),
        }
