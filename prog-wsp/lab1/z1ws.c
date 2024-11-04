#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

int arrA[] = {1, 4, 9, 13};
int arrB[] = {2, 3, 4, 8, 11};
int arrC[] = {4, 5, 12};

int i = 0, j = 0, k = 0;

void* p(void* a) {
    int* ans;
    while(!(arrA[i] == arrB[j] && arrB[j] == arrC[k])) {
        if(arrA[i] < arrB[j])
            i++;
    }
}

void* q(void* a) {
    while(!(arrA[i] == arrB[j] && arrB[j] == arrC[k])) {
        if(arrB[j] < arrC[k])
            j++;
    }
}

void* r(void* a) {
    while(!(arrA[i] == arrB[j] && arrB[j] == arrC[k])) {
        if(arrC[k] < arrA[i])
            k++;
    }
}

int main(){
   pthread_t t1, t2, t3;

   pthread_create(&t1, NULL, p, NULL);
   pthread_create(&t2, NULL, q, NULL);
   pthread_create(&t3, NULL, r, NULL);

   pthread_join(t1, NULL);
   pthread_join(t2, NULL);
   pthread_join(t3, NULL);

   printf("%d; %d; %d\n", i, j, k);
   return 0;
}
