/*************  Korzystanie z semaforów  ******************************

Deklaracje w pliku semaphore.h

sem_t       - typ semaforowy  
sem_init(sem_t* s, 0, wartość_semafora) - inicjowanie wartością początkową semafora  
sem_post(sem_t* s)  - operacja signal  
sem_wait(sem_t* s)  - operacja wait  

**************************************************************/

  
/*************  Korzystanie z mutexu  ******************************

pthread_mutex_t   - typ mutexowy

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;  // inicjowanie (mutex otwarty) (domyślna inicjalizacja)  
pthread_mutex_lock(pthread_mutex_t* m)  - zamknięcie mutexu  
pthread_mutex_unlock(pthread_mutex_t* m)  - otwarcie mutexu  

Uwaga:  
- operacja pthread_mutex_lock blokuje wątek, gdy mutex jest zamknięty  
- operacja pthread_mutex_unlock jest niezdefiniowana, gdy mutex jest otwarty  

**************************************************************/

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

sem_t sem_p, sem_qr;

int x;

#define ITER 10

void* p(void* l) {  // funkcja wątku (wątek)
    int i, j = 0;

    for (i = 0; i < ITER; i++) {
        j = j + 1;
        sem_wait(&sem_p);
        x = j;
        sem_post(&sem_qr);

    }

    return 0;     
}

void* q(void* l) {  // function for thread q
    int n = (long)l;
    int i;

    for (i = 0; i < ITER / 2; i++) {
        sem_wait(&sem_qr);
        if (x % 2 == 1) {
            printf("Thread %d printed %d\n", n, x);
        }
        sem_post(&sem_p);
    }

    return 0;
}

void* r(void* l) {  // function for thread r
    int n = (long)l;
    int i;

    for (i = 0; i < ITER / 2; i++) {
        sem_wait(&sem_qr);
        if (x % 2 == 0) {
            printf("Thread %d printed %d\n", n, x);
        }
        sem_post(&sem_p);
    }

    return 0;
}

int main() {
    pthread_t w1, w2, w3;

    sem_init(&sem_p, 0, 1);
    sem_init(&sem_qr, 0, 0);
    // sem_init(&sem_r, 0, 0);

    pthread_create(&w1, NULL, p, NULL);          // tworzy nowy wątek
    pthread_create(&w2, NULL, q, (void*)1);      // tworzy nowy wątek
    pthread_create(&w3, NULL, r, (void*)2);      // tworzy nowy wątek

    pthread_join(w1, NULL);  // czeka na zakończenie wątku 1
    pthread_join(w2, NULL);  
    pthread_join(w3, NULL);

    sem_destroy(&sem_p);
    sem_destroy(&sem_qr);

    return 0;
}
