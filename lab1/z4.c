#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

#define SIZE 100000000
int arr[SIZE];

void* p(void* a){
    int* arr = (int*) a;
    int* ans = (int*)malloc(sizeof(int)); 
    *ans = *arr;

    for (int i = 0; i < SIZE/2; i++)
        if (*ans > arr[i])
            *ans = arr[i];

    return (void*) ans;
}

void* q(void* a){
    int* arr = (int*) a;
    int* ans = (int*)malloc(sizeof(int)); 
    *ans = *arr;

    for (int i = SIZE/2; i < SIZE; i++)
        if (*ans > arr[i])
            *ans = arr[i];

    return (void*) ans;
}

int main() {
    srand(time(NULL));
    for (int i = 0; i < SIZE; i++){
        arr[i] = rand() % 1000 + 1;
    }

    pthread_t t1, t2;
    int *ans1, *ans2;

    pthread_create(&t1, NULL, p, (void*) arr);
    pthread_create(&t2, NULL, q, (void*) arr);

    pthread_join(t1, (void**)&ans1);
    pthread_join(t2, (void**)&ans2);

    // for (int i = 0; i < SIZE; i++)
    //     printf("%d, ", arr[i]);

    printf("\nmin val = %d \n", (*ans1 < *ans2) ? *ans1 : *ans2);
    return 0;
}
