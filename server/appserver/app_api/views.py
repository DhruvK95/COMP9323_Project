from django.http import HttpResponse
from rest_framework.generics import ListAPIView, RetrieveAPIView, UpdateAPIView, DestroyAPIView, CreateAPIView
from rest_framework.views import APIView
from rest_framework.decorators import permission_classes
from rest_framework.permissions import AllowAny, IsAuthenticated
from .serializers import *
from rest_framework.response import Response
from django.http import Http404

def index(request):
    return HttpResponse("Hello, world.")


@permission_classes((AllowAny,))
class MobileUserAPIView(ListAPIView):
    queryset = MobileUser.objects.all()
    serializer_class = MobileUserCreateSerializer


@permission_classes((AllowAny,))
class MobileUserCreateAPIView(CreateAPIView):
    queryset = MobileUser.objects.all()
    serializer_class = MobileUserSerializer


@permission_classes((AllowAny,))
class MobileUserDetailAPIView(RetrieveAPIView):
    queryset = MobileUser.objects.all()
    serializer_class = MobileUserSerializer


@permission_classes((AllowAny,))
class MobileUserUpdateAPIView(UpdateAPIView):
    queryset = MobileUser.objects.all()
    serializer_class = MobileUserSerializer


@permission_classes((AllowAny,))
class MobileUserDestroyAPIView(DestroyAPIView):
    queryset = MobileUser.objects.all()
    serializer_class = MobileUserSerializer


# Events
@permission_classes((AllowAny,))
class EventListAPIView(ListAPIView):
    queryset = Event.objects.all()
    serializer_class = EventSerializer


@permission_classes((AllowAny,))
class EventCreateAPIView(CreateAPIView):
    queryset = Event.objects.all()
    serializer_class = EventCreateUpdateSerializer


@permission_classes((AllowAny,))
class EventUpdateAPIView(UpdateAPIView):
    queryset = Event.objects.all()
    serializer_class = EventCreateUpdateSerializer


@permission_classes((AllowAny,))
class EventDetailAPIView(RetrieveAPIView):
    queryset = MobileUser.objects.all()
    serializer_class = EventSerializer


@permission_classes((AllowAny,))
class EventDestroyAPIView(DestroyAPIView):
    queryset = Event.objects.all()
    serializer_class = EventSerializer


# Food Deals
@permission_classes((AllowAny,))
class FoodDealListAPIView(ListAPIView):
    queryset = FoodDeal.objects.all()
    serializer_class = FoodDealSerializer


@permission_classes((AllowAny,))
class FoodDealCreateAPIView(CreateAPIView):
    queryset = FoodDeal.objects.all()
    serializer_class = FoodDealCreateUpdateSerializer


@permission_classes((AllowAny,))
class FoodDealUpdateAPIView(UpdateAPIView):
    queryset = FoodDeal.objects.all()
    serializer_class = FoodDealCreateUpdateSerializer


@permission_classes((AllowAny,))
class FoodDealDetailAPIView(RetrieveAPIView):
    queryset = FoodDeal.objects.all()
    serializer_class = FoodDealSerializer


@permission_classes((AllowAny,))
class FoodDealDestroyAPIView(DestroyAPIView):
    queryset = FoodDeal.objects.all()
    serializer_class = FoodDealSerializer
