/*****************************  ćwiczenie *********************************
ćwiczenie: 
Działa ustalona liczba wątków, które generują liczby losowe. Każdy wątek wykonuje jednakową liczbę prób, w których losowana jest kolejna liczba. 
Wątek główny ma obliczyć i wypisać wartość średnią liczb uzyskanych 
przez wątki w każdej kolejnej próbie.

wsk. wykład_2: przykład 2
Przykładowy program bez synchronizacji znajduje się poniżej. 

***************************************************************************/

#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>

#define ILOSC_PROB 10
#define liczba_watkow 3

sem_t semafor_glowny;
sem_t semafor_watek[liczba_watkow]; 
pthread_mutex_t mutex;
float t[liczba_watkow];

void* p(void* k) {
    long idxOfThr = (long)k;
    int randomNum;

    for (int i = 0; i < ILOSC_PROB; i++) {
        randomNum = rand() % 100;

        sem_wait(&semafor_watek[idxOfThr]); 

        pthread_mutex_lock(&mutex);
        t[idxOfThr] = randomNum; 
        pthread_mutex_unlock(&mutex);

        sem_post(&semafor_glowny); 
    }

    return 0;
}

int main(int arg, char **argv) {
    srand(time(NULL)); 
    sem_init(&semafor_glowny, 0, 0); 
    pthread_mutex_init(&mutex, NULL); 

    pthread_t arrayOfThreads[liczba_watkow];

    for (int i = 0; i < liczba_watkow; i++) 
        sem_init(&semafor_watek[i], 0, 0);
    
    for (long i = 0; i < liczba_watkow; i++) 
        pthread_create(&arrayOfThreads[i], NULL, p, (void*)i);
    

    float suma = 0;
    for (int i = 0; i < ILOSC_PROB; i++) {
        suma = 0;

        for (int j = 0; j < liczba_watkow; j++) 
            sem_post(&semafor_watek[j]);

        for (int j = 0; j < liczba_watkow; j++) 
            sem_wait(&semafor_glowny);

        for (int j = 0; j < liczba_watkow; j++) 
            suma += t[j];

        printf("proba[%d] [%.2lf, %.2lf, %.2lf] = %.2lf\n", i+1, t[0], t[1], t[2], suma / liczba_watkow);
    }

    for (long i = 0; i < liczba_watkow; i++) 
        pthread_join(arrayOfThreads[i], NULL);

    for (int i = 0; i < liczba_watkow; i++) 
        sem_destroy(&semafor_watek[i]);
    
    sem_destroy(&semafor_glowny);
    pthread_mutex_destroy(&mutex);

    return 0;
}

