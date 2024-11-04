#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

#define maxNoOfThreads 5
sem_t semafor;


void* p(void* arg){
    int *n = (int*) arg;
    int sum = 0;

    for (int i = 1; i <= *n; i++)
        sum += i;

    printf("suma liczb N <= [%d] = %d\n", *n, sum);
    sem_post(&semafor);
    return NULL;
}

int main(){
    FILE *file = fopen("numbers.txt", "r");
    if (file == NULL){
        printf("\nerror pliku\n");
        return 1;
    }

    pthread_t threads[maxNoOfThreads];
    sem_init(&semafor, 0, maxNoOfThreads);
    int number;
    int threadCount = 0;


    while (fscanf(file, "%d", &number) != EOF){
        int* num = malloc(sizeof(int));
        *num = number;

        sem_wait(&semafor);
        pthread_create(&threads[threadCount], NULL, p, num);
        
        threadCount = (threadCount + 1) % maxNoOfThreads;
    }
    fclose(file);

    for (int i = 0; i < maxNoOfThreads; ++i) {
        sem_wait(&semafor);
    }

    return 0;
}