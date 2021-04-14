#include <iostream>


void swap(int* arr, int a, int b){
    int temp;
    temp = arr[a];
    arr[a] = arr[b];
    arr[b] = temp;
    
}

void Selection_sort(int* arr, int size){
    int Min_idx; // smallest values IDX
    for(int i = 0; i < size; i++){
        Min_idx = i;
        for(int j = i + 1; j < size ; j++){
            if(arr[Min_idx] > arr[j]){
                Min_idx = j;
            }

        }
        
        if(Min_idx != i){
            swap(arr, i , Min_idx);
        }
        
    }
    
}

int main(){
    int arr[10] = { 10, 5, 8, 4, 6, 7, 9, 1, 2, 3};
    int size = sizeof(arr)/sizeof(int);
    Selection_sort(arr,size);
        
    for(int i = 0; i < 10 ; i++){
    std::cout << arr[i] << " ";
    }
    
}