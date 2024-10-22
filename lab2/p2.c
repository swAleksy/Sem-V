/*************  korzystanie z semaforów  ******************************

deklaracje w pliku semaphore.h

sem_t         - typ semaforowy
sem_init(sem_t* s, 0, wartość_semafora) - inicjowanie wartością początkową semafora
sem_post(sem_t* s)     - operacja signal
sem_wait(sem_t* s)     - operacja wait

**************************************************************/

/*************  korzystanie z mutexu  ******************************

pthread_mutex_t         - typ mutexowy

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER - inicjowanie (mutex otwarty) (domyślna inicjalizacja)
pthread_mutex_lock(pthread_mutex* m)    - zamknięcie mutexu, operacja atomowa
pthread_mutex_unlock(pthread_mutex* m)  - otwarcie mutexu, operacja atomowa

uwaga: 
- operacja pthread_mutex_lock blokuje wątek usiłujący zamknąć mutex, który jest zamknięty przez inny wątek 
- operacja pthread_mutex_unlock jest niezdefiniowana, gdy mutex jest otwarty

**************************************************************/

/*********************** ćwiczenie **************************

Funkcje w programach współbieżnych, które można wywoływać bezpiecznie. 

ćwiczenia:
a) Skompilować i uruchomić program. Zaobserwować wydruk programu.
b) Zakomentować instrukcje: mutex_lock, mutex_unlock. 
Skompilować i uruchomić program. Zaobserwować wydruk programu.  

Zwrócić uwagę na:
- sposób zdefiniowania funkcji wypisz
- co by się stało, gdyby wątek zakończył działanie w czasie wykonywania funkcji wypisz (sprawdzić)

************************************************************/

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

#define MIT 1000

pthread_mutex_t blokada = PTHREAD_MUTEX_INITIALIZER; // deklaracja + inicjalizacja mutexu

void wypisz(int k) {
    // pthread_mutex_lock(&blokada);
    
    int i = 0;
    while (i < 50) {
        i++;
        printf("%d", k);
        fflush(stdout);
    }
    printf("\n");
    
    // pthread_mutex_unlock(&blokada);
}

void* p(void* l) { // funkcja wątku (wątek)
    int i;
    for (i = 0; i < MIT; i++) 
        wypisz(1);
    return 0;
}

void* q(void* l) { // funkcja wątku (wątek)
    int i;
    for (i = 0; i < MIT; i++) 
        wypisz(2);
    return 0;
}

int main() {
    pthread_t w1, w2;
    
    pthread_create(&w1, 0, p, 0); // tworzy wątek dla funkcji p
    pthread_create(&w2, 0, q, 0); // tworzy wątek dla funkcji q
    
    pthread_join(w1, NULL); // zatrzymuje się, aż zakończy się wątek p
    pthread_join(w2, NULL); // zatrzymuje się, aż zakończy się wątek q
    
    printf("\nkoniec procesu\n");
    
    return 0;
}
