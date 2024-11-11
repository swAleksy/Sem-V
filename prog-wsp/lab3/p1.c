/*************  Korzystanie z semaforów  ******************************

Deklaracje w pliku semaphore.h

sem_t                      - typ semaforowy
sem_init(sem_t* s, 0, wartość_semafora) - inicjowanie wartością początkową semafora
sem_post(sem_t* s)         - operacja signal
sem_wait(sem_t* s)         - operacja wait

**************************************************************/

/*************  Korzystanie z mutexu  ******************************

pthread_mutex_t                   - typ mutexowy

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER - inicjowanie (mutex otwarty) (domyślna inicjalizacja)
pthread_mutex_lock(pthread_mutex* m)              - zamknięcie mutexu
pthread_mutex_unlock(pthread_mutex* m)            - otwarcie mutexu

Uwaga: 
- Operacja pthread_mutex_lock blokuje wątek, gdy mutex jest zamknięty.
- Operacja pthread_mutex_unlock jest niezdefiniowana, gdy mutex jest otwarty.

**************************************************************/

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

int x;
sem_t sem1;
sem_t sem2;

#define iter 10

void* p(void* l) { // Funkcja watku (wątek)
    int i;
    for (i = 0; i < iter; i++) {
        sem_wait(&sem1);
        x = i;
        sem_post(&sem2);
    }
    return 0;
}

void* q(void* l) { // Funkcja watku (wątek)
    int i;
    for (i = 0; i < iter; i++) {
        sem_wait(&sem2);
        printf("x = %d\n", x);
        sem_post(&sem1);
    }
    return 0;
}

int main() {
    pthread_t w1, w2;
    sem_init(&sem1, 0, 1);
    sem_init(&sem2, 0, 0);

    pthread_create(&w1, 0, p, 0); // Tworzy nowy wątek
    pthread_create(&w2, 0, q, 0); // Tworzy nowy wątek

    pthread_join(w1, 0); // Czeka na zakończenie wątku 1
    pthread_join(w2, 0); // Czeka na zakończenie wątku 2

    return 0;
}
