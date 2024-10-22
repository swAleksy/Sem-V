#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

#define MAXTHREADS 10
#define SIZE 10000000

int suma = 0;
int arr[SIZE];

void* p(void* a){
    int* subArrPtr = (int*) a;
    int* endPtr = subArrPtr + SIZE/MAXTHREADS;
    int* ans = (int*)malloc(sizeof(int)); 
    *ans = 0;
    while(subArrPtr != endPtr){
        *ans += *subArrPtr;
        subArrPtr++;
    }
    return (void*) ans;
}

int main() {
    srand(time(NULL));
    for (int i = 0; i < SIZE; i++){
        arr[i] = rand() % 10 + 1;
    }

    pthread_t threads[MAXTHREADS];

    for (int i = 0; i < MAXTHREADS; i++) {
        if (pthread_create(&threads[i], NULL, p, (void *)(arr + (SIZE / MAXTHREADS) * i)) != 0) {
            printf("\nFailed to create thread\n");
            return 1;
        }
    }

    for (int i = 0; i < MAXTHREADS; i++){
        int *temp;
        pthread_join(threads[i], (void**)&temp);
        suma += *temp;
        free(temp);
    }

    printf("\n suma = %d\n", suma);
    int suma2 = 0;
    for (int i = 0; i < SIZE; i++){
        suma2 += arr[i];
    }
    printf("\n suma2 = %d\n", suma2);
    return 0;
}
