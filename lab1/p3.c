/*************  watki - wzajemne wykluczanie ***************
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


/***************************** ćwiczenie *************************************

Program zliczający liczbę cykli wykonywanych przez funkcje p i q

1) Uruchom kilka razy program i sprawdź wyniki
2) Ustal stałą MAXL na 10 i sprawdź wyniki
Pytanie:
Objaśnić dlaczego program nie zawsze poprawnie zlicza liczbę cykli

3) Spróbuj poprawić funkcje wątku ( p,q ) w taki sposób aby zmienna licznik
   zliczała poprawnie  liczbę cykli wykonanych łącznie przez dwa wątki w
   petlach for. Wolno zmieniać tylko funkcję ( p,q )

******************************************************************************/
#define MAXL 1000000
double licznik=0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

// void* p(void* a) { // funkcja watku (watek)
//     int i;
//     double t;

//     for (i = 0; i < MAXL; i++ ) {
//     t = licznik;
//     licznik = t + 1;
//     }

//     return 0;
// }

// void* q(void* a) { // funkcja watku (watek)
//     int i;
//     double t;

//     for(i = 0; i < MAXL; i++ ) {
//     t = licznik;
//     licznik = t + 1;
//     }

//     return 0;
// }


void* p(void* a) { // funkcja watku (watek)
    int i;
    double t;

    for (i = 0; i < MAXL; i++ ) {
        pthread_mutex_lock(&mutex);  // Blokowanie muteksu przed dostępem do licznika
        t = licznik;
        licznik = t + 1;
        pthread_mutex_unlock(&mutex); // Odblokowanie muteksu po operacji na liczniku
    }

    return 0;
}

void* q(void* a) { // funkcja watku (watek)
    int i;
    double t;

    for(i = 0; i < MAXL; i++ ) {
        pthread_mutex_lock(&mutex);  // Blokowanie muteksu przed dostępem do licznika
        t = licznik;
        licznik = t + 1;
        pthread_mutex_unlock(&mutex); // Odblokowanie muteksu po operacji na liczniku
    }

    return 0;
}

int main() {
    pthread_t w1,w2;

    pthread_create(&w1, 0, p, 0); // tworzy watek dla funkcji p
    pthread_create(&w2, 0, q, 0); // tworzy watek dla funkcji p

    pthread_join(w1, NULL);
    pthread_join(w2, NULL);

    printf ("licznik=%.0lf  \n",licznik);

    printf("\nkoniec procesu\n");


    return 0;
}
