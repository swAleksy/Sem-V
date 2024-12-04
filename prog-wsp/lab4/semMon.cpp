#include <stdio.h>
#include <pthread.h>

class Semaphore {
private:
    int value;
    pthread_mutex_t mutex;
    pthread_cond_t condition;

public:
    Semaphore(int initValue) : value(initValue) {
        pthread_mutex_init(&mutex, nullptr);
        pthread_cond_init(&condition, nullptr);
    }

    ~Semaphore() {
        pthread_mutex_destroy(&mutex);
        pthread_cond_destroy(&condition);
    }

    void wait() {
        pthread_mutex_lock(&mutex);
        while (value <= 0) {
            pthread_cond_wait(&condition, &mutex);
        }
        value--;
        pthread_mutex_unlock(&mutex);
    }

    void signal() {
        pthread_mutex_lock(&mutex);
        value++;
        pthread_cond_signal(&condition);
        pthread_mutex_unlock(&mutex);
    }
};
