/*
 * GccApplication1.c
 *
 * Created: 2021-06-19 오후 1:14:58
 * Author : 2003c
 */ 

#include "glcd.h"	//User defined LCD library which conatins the lcd routines

/* start the main program */
void main()
{
	GLCD_Init();
	GLCD_Printf("Hello World!");
	GLCD_GoToLine(4);
	GLCD_Printf("*&^%$#@!~");
	GLCD_GoToLine(7);
	GLCD_DisplayString("Well this is the end!");
	
	while(1);
	
}

/*
const int board_X = 20;
const int board_Y = 10;

enum Maptype = {wall = 0, snake, apple};

struct snakeNode{
	int Xpos;
	int Ypos;
	bool is_head;
	bool is_tail;
	snakeNode* Next;
	};
	
struct Snake{
	snakeNode* head;
	int direction;
	int length;
	};
	
int Board[board_X][board_Y];

//초기 스네이크 생성
Snake SnakeInit(Snake S){
		S.head = malloc(sizeof(snakeNode))
		S.head->Xpos = 10;
		S.head->Ypos = 5;
		S.head->is_head = true;
		S.head->is_tail = false;
		snakeNode *temp1 = malloc(sizeof(snakeNode))
		
		
		
		
};

void Game_init(int B[board_X][board_Y], Snake S, int MapId){
	//벽 생성
	for(int i = 0; i < 2; i++){
		for(int j = 0; j < 20; j++){
			if(i == 0)
				B[i][0] = wall;
			if(i == 1)
				B[i][10] == wall;	
		}
	}
	for(int i = 1; i < 10; i++){
		B[0][i] == wall;
		B[19][i] == wall
	}
	
	
};

void Generate_Apple(int B[board_X][board_Y]){
			
	
	};

int main(void)
{	
	int x,y;
	
	GLCD_ON();
	
    /* Replace with your application code 
    while (1) 
    {
		for(x = 0; x<128; x+=2){
			for(y=0; y<8; y++){
				GLCD_XY(x,y);
				GLCD_Write(1);
    }
}

*/