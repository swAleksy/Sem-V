#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

#define MAXSIZE 10
int suma[MAXSIZE];

typedef struct {
    int number;
    int index;
} thread_data_t;

void* p(void* a){
    thread_data_t *data = (thread_data_t *) a;
    int number = data->number;
    int index = data->index;
    
    double sum = 0;

    for (int i = 1; i <= number; i++) {
        sum += i;
    }
    
    suma[index] = sum; 
    free(data);
    pthread_exit(NULL);
}

int main(){
    pthread_t threads[MAXSIZE];
    int numbers[MAXSIZE];
    int numCount = 0;

    FILE *file = fopen("input.txt", "r");
    if (file == NULL){
        printf("\nerror pliku\n");
        return 1;
    }

    while (fscanf(file, "%d", &numbers[numCount]) != EOF){
        numCount++;
        printf("%d\n", numCount);
        if (numCount > MAXSIZE){
            printf("\nerror liczyb\n");
            break;
        }
    }
    fclose(file);

    for (int i = 0; i < numCount; i++){
        thread_data_t *data = (thread_data_t *) malloc(sizeof(thread_data_t));
        data->number = numbers[i];
        data->index = i;

        if (pthread_create(&threads[i], NULL, p, (void *)data) != 0) {
            printf("\nNie udało się utworzyć wątku");
            return 1;
        }
    }

    for (int i = 0; i < numCount; i++)
        pthread_join(threads[i], NULL);
    

    for (int i = 0; i < numCount; i++) {
        printf("=> %d: %d\n", numbers[i], suma[i]);
    }   

    return 0;
}