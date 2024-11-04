#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

#define SIZE 100

int arr[SIZE];

void* p(void* a) {
    int* arr = (int*)a;
    int* ansArr = (int*)malloc(SIZE * sizeof(int)); 
    int idx = 0;

    for (int i = 0; i < SIZE; i++){
        if (arr[i] % 3 == 0) 
            ansArr[idx++] = arr[i]; 
    }

    return (void*)ansArr;
}

void* q(void* a){
    int* arr = (int*)a;
    int* ansArr = (int*)malloc(SIZE * sizeof(int)); 
    int idx = 0;

    for (int i = 0; i < SIZE; i++){
        if (arr[i] % 5 == 0) 
            ansArr[idx++] = arr[i]; 
    }

    return (void*)ansArr;
}

void* r(void* a){
    int* arr = (int*)a;
    int* ansArr = (int*)malloc(SIZE * sizeof(int)); 
    int idx = 0;

    for (int i = 0; i < SIZE; i++){
        if (arr[i] % 7 == 0) 
            ansArr[idx++] = arr[i]; 
    }

    return (void*)ansArr;
}

int main() {
    srand(time(NULL));
    
    for (int i = 0; i < SIZE; i++){
        arr[i] = rand() % 1000 + 1;
    }

    int *arrDivByThree, *arrDivByFive, *arrDivBySeven;

    pthread_t t1, t2, t3;

    pthread_create(&t1, NULL, p, (void*) arr);
    pthread_create(&t2, NULL, q, (void*) arr);
    pthread_create(&t3, NULL, r, (void*) arr);

    pthread_join(t1, (void**)&arrDivByThree);
    pthread_join(t2, (void**)&arrDivByFive);
    pthread_join(t3, (void**)&arrDivBySeven);

    printf("Num div by 3:\n");
    for (int i = 0; i < SIZE && arrDivByThree[i] != 0; i++)
        printf("%d, ", arrDivByThree[i]);

    printf("\nNum div by 5:\n");
    for (int i = 0; i < SIZE && arrDivByFive[i] != 0; i++)
        printf("%d, ", arrDivByFive[i]);

    printf("\nNum div by 7:\n");
    for (int i = 0; i < SIZE && arrDivBySeven[i] != 0; i++)
        printf("%d, ", arrDivBySeven[i]);

    free(arrDivByThree); 
    free(arrDivByFive); 
    free(arrDivBySeven); 
    return 0;
}
