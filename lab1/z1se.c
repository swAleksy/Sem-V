#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

int arrA[] = {1, 4, 9, 13};
int arrB[] = {2, 3, 4, 8, 11};
int arrC[] = {4, 5, 12};

int main(){
    int i, j, k = 0;

    while(1) {
        if (arrA[i] < arrB[j])
            i = i + 1;
        else if (arrB[j] < arrC[k])
            j = j + 1;
        else if (arrC[k] < arrA[i])
            k = k + 1;
        else break;
    }

    printf("idxs: i=%d, j=%d, k=%d\nnum:%d\n", i, j, k, arrA[i]);
    return 0;
}
