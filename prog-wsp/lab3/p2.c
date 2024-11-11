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

int x[2];  // Shared buffer (2-element)
sem_t sem_p_ready, sem_q_ready;      // Signals for data availability from p and q
sem_t sem_p_consumed, sem_q_consumed; // Signals for each producer to wait until consumption

#define ITER 5  // Number of iterations for each producer

void* p(void* l) {
    int i, j = 0;

    for (i = 0; i < ITER; i++) {
        j += 2;                     // Increase j by 2 (production)
        
        sem_wait(&sem_p_consumed);   // Wait until the consumer reads previous data
        x[0] = j;                    // Write value to x[0]
        sem_post(&sem_p_ready);      // Signal to consumer that `x[0]` is ready
    }

    return NULL;     
}

void* q(void* l) {
    int i, j = -1;

    for (i = 0; i < ITER; i++) {
        j += 2;                      // Increase j by 2 (production)
        
        sem_wait(&sem_q_consumed);   // Wait until the consumer reads previous data
        x[1] = j;                    // Write value to x[1]
        sem_post(&sem_q_ready);      // Signal to consumer that `x[1]` is ready
    }

    return NULL;     
}

// Consumer thread function `r`
void* r(void* l) {
    int i;

    for (i = 0; i < ITER; i++) {
        sem_wait(&sem_p_ready);      // Wait for data from producer `p`
        sem_wait(&sem_q_ready);      // Wait for data from producer `q`
        
        printf("%d\n%d\n", x[0], x[1]); // Read and print data from buffer

        sem_post(&sem_p_consumed);   // Signal to producer `p` that it can produce again
        sem_post(&sem_q_consumed);   // Signal to producer `q` that it can produce again
    }

    return NULL;     
}

int main() {
    pthread_t w1, w2, w3;

    // Initialize semaphores
    sem_init(&sem_p_ready, 0, 0);    // Initially, consumer waits for data from `p`
    sem_init(&sem_q_ready, 0, 0);    // Initially, consumer waits for data from `q`
    sem_init(&sem_p_consumed, 0, 1); // Initially, allow producer `p` to produce
    sem_init(&sem_q_consumed, 0, 1); // Initially, allow producer `q` to produce

    // Create producer and consumer threads
    pthread_create(&w1, NULL, p, NULL);
    pthread_create(&w2, NULL, q, NULL);
    pthread_create(&w3, NULL, r, NULL);

    // Wait for threads to finish
    pthread_join(w1, NULL);
    pthread_join(w2, NULL);
    pthread_join(w3, NULL);

    // Destroy semaphores
    sem_destroy(&sem_p_ready);
    sem_destroy(&sem_q_ready);
    sem_destroy(&sem_p_consumed);
    sem_destroy(&sem_q_consumed);

    return 0;
}
