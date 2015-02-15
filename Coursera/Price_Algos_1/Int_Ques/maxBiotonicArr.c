#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int FindMax(int *arr, int length) {
	int lidx = 0, hidx = (length - 1);
	int midx;
	
	while(lidx <= hidx) {
		midx = lidx + ((hidx - lidx)/2);

		if(midx == 0 || midx == (length - 1))
			return midx;
		
		if(arr[midx] > arr[midx-1]) {
			if(arr[midx] > arr[midx + 1]) {
				return midx;
			} else {
				lidx = midx + 1;
			}
		} else {
			hidx = midx - 1;
		}
	}
	
	return midx;
}

typedef int (*CompFunc)(const void *, const void *);

int AscCompFunc(const void *a, const void *b) {
	int l = *((int *)a);
	int r = *((int *)b);
	
	return((l > r) - (l < r));
}

int DescCompFunc(const void *a, const void *b) {
	int l = *((int *)a);
	int r = *((int *)b);
	
	return((l < r) - (l > r));
}

int BinarySearch(int *arr, int length, int key, CompFunc compfunc) {
	int low = 0, high = (length - 1);
	
	while (low <= high) {
		int mid = low + ((high - low)/2);
		
		/* Check Mid Element */
		if(compfunc(&key, (arr+mid)) == 0)
			return mid;
		/* Search Left Sub-array */
		else if(compfunc(&key, (arr+mid)) < 0) 
			high = mid -1;
		/* Search Right sub-array */
		else 
			low = mid + 1;
	}
	
	return -1;
}

int main() {
	int inparr[] = {10,11,12,13,14,15,16,5,4,3,2,1};
	int maxidx, searchidx;
	int key = 16, length = 12;
	
	maxidx = FindMax(inparr, length);
	
	searchidx = BinarySearch(inparr, (maxidx+1), key, AscCompFunc);
	
	if(searchidx == -1) {
		searchidx = BinarySearch((inparr + maxidx + 1), (length - maxidx), key, DescCompFunc);
		searchidx = (searchidx != -1) ? (maxidx + searchidx + 1) : -1;
	}
	
	printf("Key %d found at %d\n", key, searchidx);	
	
	return 0;
}