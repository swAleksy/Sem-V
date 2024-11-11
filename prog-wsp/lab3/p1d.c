#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

#define NUM_TO_GEN 50
#define BUFFER_SIZE 5  

sem_t semaphores[5];        
sem_t space_available;       
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

int buffer[BUFFER_SIZE];
int count = 0, in = 0, out = 0;
int items_generated = 0;

void* w(void* input) {
    int idx = *((int *)input);
    while (1) {
        sem_wait(&semaphores[idx - 1]);  

        pthread_mutex_lock(&mutex);
        if (count > 0) {
            buffer[out] *= 2;  

            if (idx < 5) {
                sem_post(&semaphores[idx]);  
            } else {
                printf("Final value processed by w5: %d\n", buffer[out]);
                out = (out + 1) % BUFFER_SIZE;
                count--;
                sem_post(&space_available);  
                sem_post(&semaphores[0]);
            }
        } else if (items_generated >= NUM_TO_GEN) {
            pthread_mutex_unlock(&mutex);
            break;  
        }

        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main() {
    srand(time(NULL));

    sem_init(&space_available, 0, BUFFER_SIZE);
    for (int i = 0; i < 5; i++) {
        sem_init(&semaphores[i], 0, 0);
    }

    pthread_t threads[5];
    int thread_ids[5] = {1, 2, 3, 4, 5};

    for (int i = 0; i < 5; i++) {
        pthread_create(&threads[i], NULL, w, &thread_ids[i]);
    }

    for (int i = 0; i < NUM_TO_GEN; i++) {
        int num = rand() % 100 + 1;

        sem_wait(&space_available);  
        pthread_mutex_lock(&mutex);

        buffer[in] = num;
        printf("Generated number: %d at index %d\n", num, in);
        in = (in + 1) % BUFFER_SIZE;
        count++;
        items_generated++;

        pthread_mutex_unlock(&mutex);
        if (i == 0)
            sem_post(&semaphores[0]);  
    }

    for (int i = 0; i < 5; i++) {
        pthread_join(threads[i], NULL);
    }

    pthread_mutex_destroy(&mutex);
    sem_destroy(&space_available);
    for (int i = 0; i < 5; i++) {
        sem_destroy(&semaphores[i]);
    }

    return 0;
}
