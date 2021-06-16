// DB_project2.cpp : 이 파일에는 'main' 함수가 포함됩니다. 거기서 프로그램 실행이 시작되고 종료됩니다.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <stack>
#include <string>
#include <algorithm>

using namespace std;

class Header {
public:
    int BlockSize, RootBID, Depth;
    Header() {};
    Header(fstream& file, int _headerSize) {
        char* buffer;
        file.seekg(0, ios::beg);
        file.read(buffer, 4);
        this->BlockSize = (int)buffer;
        file.read(buffer, 4);
        this->RootBID = (int)buffer;
        file.read(buffer, 4);
        this->Depth = (int)buffer;
    };
    
};

class Node {
public:
    pair<int, int>* entry; // 첫번째 key값, 두번째 BID
    int nextBID;
    int size;
    int block;
};




class BTree {
public:
    Header* header;
    stack<int> _stack;

    BTree(fstream& file, int blockSize) {
        header = new Header(file, 12);
        pair<int, int> Root = make_pair(header->RootBID, header->BlockSize);
    };
    bool insert(int key, int rid) {

    };
    void print() {

    };
    // point search
    int* search(int key) {

    }; 
    // range search
    int* search(int startRange, int endRange) {

    };
};


int main(int argc, char* argv[])
{
    char command = argv[1][0];
    const char* name = "btree.bin";
   
    fstream files(name, ios::in | ios::out | ios::binary);
    
    if (!files.is_open()) {
        cerr << "Failed to open file" << endl;
        return 0;
    };

    BTree* myBtree = new BTree(files, 2);

    switch (command)
    {
    case 'c':
        // create index file
        break;
    case 'i':
        // insert records from [records data file], ex) records.txt
        break;
    case 's':
        // search keys in [input file] and print results to [output file]
        break;
    case 'r':
        // search keys in [input file] and print results to [output file]
        break;
    case 'p':
        // print B+-Tree structure to [output file]
        break;
    }

// 프로그램 실행: <Ctrl+F5> 또는 [디버그] > [디버깅하지 않고 시작] 메뉴
// 프로그램 디버그: <F5> 키 또는 [디버그] > [디버깅 시작] 메뉴

// 시작을 위한 팁: 
//   1. [솔루션 탐색기] 창을 사용하여 파일을 추가/관리합니다.
//   2. [팀 탐색기] 창을 사용하여 소스 제어에 연결합니다.
//   3. [출력] 창을 사용하여 빌드 출력 및 기타 메시지를 확인합니다.
//   4. [오류 목록] 창을 사용하여 오류를 봅니다.
//   5. [프로젝트] > [새 항목 추가]로 이동하여 새 코드 파일을 만들거나, [프로젝트] > [기존 항목 추가]로 이동하여 기존 코드 파일을 프로젝트에 추가합니다.
//   6. 나중에 이 프로젝트를 다시 열려면 [파일] > [열기] > [프로젝트]로 이동하고 .sln 파일을 선택합니다.
