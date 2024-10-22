/*************  kontrola liczby watkow - semafory **************
Funkcja tworzenia watku:
  pthread_create(&w, &atw, watek,(void*)par);
  w   	- obiekt watku
  atw 	- obiekt atrybutu watku ( do ustawienia atrybutow tworzonego watku)
  watek - funkcja ktora wykonuje sie jako nowy watek
  par   - parametr funkcji watek

Funkcja watku:
 void *fw( void *) - prototyp funkcji watku
 Funkcje watku dzialaja wspolbieznie (rownolegle) po utworzeniu
 watku (wywolanie  pthread_create ) w ramach tego samego procesu.
 Zmienne globalne programu sa wspolne dla wszystkich watkow

**************************************************************/



#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>


/******************************* ćwiczenie *******************************
Program pokazuje różnicę między wątkiem trwałym a ulotnym

-tworzenie watkow trwałych odbywa się z atrybutem PTHREAD_CREATE_JOINABLE
-tworzenie watkow ulotnych odbywa się z atrybutem PTHREAD_CREATE_DETACHED

1) uruchom program w którym tworzone są wątki trwałe
2) uruchom program w którym tworzone są wątki ulotne

Objaśnić różnicę w działaniu programu w wersjach (1) i (2)


Ważne:
Wątków trwałych można tworzyć tylko ograniczoną liczbę mimo
że czas życia ich jest tutaj bardzo krótki( wykonują tylko dwie instrukcje).
Problem polega na tym, że dla wątków trwałych w odróżnieniu od wątków ulotnych
zasoby nie są automatycznie zwalniane po zakończeniu ich działania.
Aby zasoby zakończonego wątku zostały zwolnione inny działający wątek (najlepiej główny)
powinien wywołać funkcję pthread_join. W tym przypadku funkcja pthread_join
nie jest wywoływana. Stąd duża liczba trwałych wątków musi w końcu wyczerpać
zasoby systemu i następne wątki nie będą już tworzone.
W przypadku wątku ulotnego zasoby są automatycznie zwalniane
po zakończeniu działania takiego wątku.

Zwroć uwagę:
a) jak jest przekazywana wartość całkowita do funkcji wątku.
b) jak ustalić atrybut tworzonego wątku

********************************************/
long licznik=0;

void* p (void* l) { // funkcja watku (watek)
    long n=*(long *)l;
    printf("watek %ld\n",n);

    return 0;
}

int main () {
    pthread_t w;
	pthread_attr_t atrybut;
	pthread_attr_init(&atrybut); //inicjalizuje obiekt atrybutu dla watku

    //pthread_attr_setdetachstate(&atrybut,PTHREAD_CREATE_JOINABLE); //ustawia atrybut wątek trwały
    pthread_attr_setdetachstate(&atrybut,PTHREAD_CREATE_DETACHED); //ustawia atrybut wątek ulotny

    do  {
        licznik++;
    }   while (pthread_create(&w, &atrybut, p,(void*)&licznik)==0);

        printf("\nkoniec procesu\n");


    return 0;
}
