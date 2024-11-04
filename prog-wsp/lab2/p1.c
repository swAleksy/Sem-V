/*************  korzystanie z semaforów  ******************************

deklaracje w pliku semaphore.h

sem_t        - typ semaforowy
sem_init(sem_t* s,0,wartość_semafora) - inicjowanie wartością początkową semafora
sem_post(sem_t* s)   - operacja signal
sem_wait(sem_t* s)   - operacja wait

**************************************************************/

/*************  korzystanie z mutexu  ******************************

ptheread_mutex_t       - typ mutexowy

pthread_mutex_t mutex=PTHREAD_MUTEX_INITIALIZER - inicjowanie (mutex otwarty) (domyślna inicjalizacja)
pthread_mutex_lock(pthread_mutex* m)    - zamknięcie mutexu (zajęcie)
pthread_mutex_unlock(pthread_mutex* m)  - otwarcie mutexu   (zwolnienie)
 
uwaga: 
- operacja pthread_mutex_lock blokuje wątek gdy mutex jest zamkniety (zajęty)
- operacja pthread_mutex_unlock jest niezdefiniowana gdy mutex jest otwarty (wolny)

**************************************************************/

/*********************** ćwiczenie **************************

zwrócić uwagę na:
a) do czego służy zmienna semafor w tym programie i jak się z niego korzysta
b) jak działa program gdy wartość początkową semafora ustalimy 2 (objaśnić)
             
c) sprawdzić czas działania programu
d) sprawdzić czas działania programu gdy operacje na semaforze będą wykomentowane, 
   poprawić program w taki sposób by wykonywał się możliwie najszybciej.

************************************************************/

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

double liczba = 0;

#define maks 10000000
#define wartosc_semafora 1

sem_t semafor;          // deklaracja semafora
pthread_mutex_t mutex;

void* p (void* l) { // funkcja watku (watek)
   int i = 0, temp = 0;
   for (i = 0; i < maks / 2; i++) 
        temp++;

    // pthread_mutex_lock(&mutex);
    sem_wait(&semafor);
    liczba = liczba + temp;          // sekcja krytyczna
    sem_post(&semafor);   
    // pthread_mutex_unlock(&mutex);

   return 0;     
}

void* q (void* l) { // funkcja watku (watek)
   int i = 0, temp = 0;
   for (i = 0; i < maks / 2; i++) 
        temp++;

    // pthread_mutex_lock(&mutex);
    sem_wait(&semafor);
    liczba = liczba + temp;          // sekcja krytyczna
    sem_post(&semafor);   
    // pthread_mutex_unlock(&mutex);

   return 0;     
}

int main() {
   pthread_t w1, w2;
   
   sem_init(&semafor, 0, wartosc_semafora); // inicjuje wartość semafora
   
   pthread_create(&w1, 0, p, 0);  // tworzy nowy watek       
   pthread_create(&w2, 0, q, 0);  // tworzy nowy watek                
   
   pthread_join(w1, 0);  // czeka na zakonczenie watku 1
   pthread_join(w2, 0);     
   
   printf("liczba = %.0lf", liczba);  
   
   printf("\nkoniec procesu\n");
   
   return 0;
}
