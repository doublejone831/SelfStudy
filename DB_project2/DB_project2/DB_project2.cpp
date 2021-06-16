/***********************************
DB_project2.cpp
12161990 장지원
************************************/
#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <string>
#include <algorithm>

using namespace std;

class Node {
public:
    bool IsleafNode;//true=> LeafNode, false => nonleafnode
    int* BID;// If IsleafNode == true, 0~N-1 => value, N=> next leafNode BID / OR IsleafNode == false, 0~N=> nextlevel BID;        
    int* key;// key 0~3
    int size;
    int BlockID;
    Node(fstream& fs, int _BID, int hSize, int blockSize, bool _isleaf) {
        
        BlockID = _BID;
		size = (blockSize / 4) / 2;
		key = new int[size];
		BID = new int[size + 1];
        fs.seekg(hSize + (_BID - 1) * blockSize);
        for (int count = 0; count < size; count++) {
            fs.read((char*)&BID[count], 4);
            fs.read((char*)&key[count], 4);
        }
        fs.read((char*)&BID[4], 4);
        IsleafNode = _isleaf;
    }

	Node(int blockSize, int _BID, bool _isleaf) {
		IsleafNode = _isleaf;
		size = (blockSize / 4) / 2;
		BlockID = _BID;
	}



    ~Node() {}
    friend class BPTree;
};

class BTree {
public:
	vector<int> trace;
    int headerSize;
    struct fileheader {
        int BlockSize;
        int RootBID;
        int Depth;
    };
    fileheader header;
    fstream fs;
    int lastBID;
	int entityNum; // BID갯수 = entitynum+1
	int nodecount;
    void write_header(int _BlockSize, int _RootBID, int _Depth) {
        fs.seekg(0, ios::beg);
        fs.seekp(0, ios::beg);
        fs.write((char*)&_BlockSize, 4);
        fs.write((char*)&_RootBID, 4);
        fs.write((char*)&_Depth, 4);
    }

    void write_node(Node* node, int HSize) {
        int BSize = header.BlockSize;
        fs.seekp(HSize + (node -> BlockID - 1) * BSize);
        int i;
        for (i = 0; i < node->size; i++) {
            fs.write((char*)&node->BID[i], 4);
            fs.write((char*)&node->key[i], 4);
        }
        fs.write((char*)&node->BID, i + 1);
            
    }

    BTree(const char* filename) {
        fs.open(filename, ios::in | ios::out | ios::binary);
    };
    BTree(const char* filename, int _Bsize) {
        fs.open(filename, ios::in | ios::out | ios::binary);
        header.RootBID = 0;
        header.BlockSize = _Bsize;
        header.Depth = 0;
        lastBID = 0;
        headerSize = 12;
		nodecount = 0;

        write_header(header.BlockSize, header.RootBID, header.Depth);
    };

    void init() {
        fs.seekg(0);
        fs.read((char*)&header.BlockSize, 4);
        fs.read((char*)&header.RootBID, 4);
        fs.read((char*)&header.Depth, 4);
        headerSize = 12;
		entityNum = (header.BlockSize / 4) / 2;
    }

	Node* search(int _key) {
		Node* Noneleaf;
		Node* Leaf;

		int cur_node = header.RootBID;
		trace.push_back(cur_node);
		int cur_height = 0;

		//Empty Tree인 경우
		if (header.RootBID == 0) {
			return NULL;
		}
		//Empty Tree가 아닌경우
		while (cur_height < header.Depth - 1) {
			Noneleaf = new Node(fs, cur_node, headerSize, header.BlockSize, false);
			for (int i = 0; i < entityNum - 1; i++) {
				//  키가 작은경우
				if (_key < Noneleaf->key[i]) {
					cur_node = Noneleaf->BID[i];
					trace.push_back(cur_node);
					break;
				}
				//키가 같은경우
				if (_key = Noneleaf->key[i]) {
					cur_node = Noneleaf->BID[i + 1];
					trace.push_back(cur_node);
					break;
				}
				//마지막까지 탐색에서도 걸러지지 않은 경우
				if (i == entityNum - 1) {
					cur_node = Noneleaf->BID[i + 1];
					trace.push_back(cur_node);
					break;
				}
			}
			cur_height++;
		}
		Leaf = new Node(fs, cur_node, headerSize, header.BlockSize, true);
		return Leaf;
	}

	bool insert(int key, int ID) {
		init();
		fs.seekg(-1, ios::end);
		lastBID = fs.tellg() / header.BlockSize;

		//Empty tree인경우
		if (header.RootBID == 0) {
			header.RootBID = 1;
			header.Depth++;
			write_header(header.BlockSize, header.RootBID, header.Depth);
			for (int count = 0; count < entityNum; count++) {
				fs.write((char*)&key, 4);
				fs.write((char*)&ID, 4);
				key = 0;
				ID = 0;
			}
			fs.write("\0", 4);
		}
		// Empty tree가 아닌경우
		else {

		}

	}

	//BFS로 순회하면 레벨별로 출력
	/*
	void print(fstream& os) {
		init();

		Node* node = new Node(fs, header.RootBID, headerSize, header.BlockSize, false);
		Node* temp;
		Node * leafNode;

		bool* visit = new bool[nodecount];
		queue<Node*> q;
		q.push(node);

		while (!q.empty()) {
			Node* x = q.front();
			q.pop();
			printNode(os, x);
			for (int i = 0; i < ((x->size / 4) / 2) + 1; i++) {
				if (!visit[i]) {
					q.push
				}
			}
		}

	}

	void printNode(fstream& os, Node* n) {
		for (int i = 0; i < entityNum; i++) {
			os << n->key[i] << ",";
		}
	}
	*/
};

int main(int argc, char* argv[]) {
	char command = argv[1][0];
	char* binFile = argv[2];
	int initSize;
	int key, ID;
	int rangeStart, rangeEnd;
	BTree* myBPtree;
	fstream fs;
	fstream input;
	fstream output;

	string str;
	string strTmp;

	vector<pair<int, int>> entryVec;

	switch (command) {
	case 'c':
		// create index file
		initSize = atoi(argv[3]);
		myBPtree = new BTree(binFile, initSize);
		break;
	case 'i':
		// insert records from [records data file], ex) records.txt
		myBPtree = new BTree(binFile);
		input.open(argv[3], ios::in);
		while (getline(input, str)) {
			strTmp = (str.substr(0, str.find_first_of(",")));
			str = str.substr(strTmp.size() + 1, str.size());
			key = atoi(strTmp.c_str());
			ID = atoi(str.c_str());

			myBPtree->insert(key, ID);
		}
		fs.close();
		delete myBPtree;
		break;
	case 's':
		// search keys in [input file] and print results to [output file]
		myBPtree = new BTree(binFile);
		myBPtree->init();
		input.open(argv[3], ios::in);
		output.open(argv[4], ios::out);
		while (getline(input, str)) {
			key = atoi(str.c_str());
			output << myBPtree->search(key) << "\n";
		}
		output.close();
		fs.close();
		delete myBPtree;
		break;
	case 'r':
		// search keys in [input file] and print results to [output file]
		myBPtree = new BTree(binFile);
		myBPtree->init();
		input.open(argv[3], ios::in);
		output.open(argv[4], ios::out);
		while (getline(input, str)) {
			strTmp = (str.substr(0, str.find_first_of(",")));
			str = str.substr(strTmp.size() + 1, str.size());
			rangeStart = atoi(strTmp.c_str());
			rangeEnd = atoi(str.c_str());

			entryVec = myBPtree->search(rangeStart, rangeEnd);
			for (vector<pair<int, int>>::iterator it = entryVec.begin(); it != entryVec.end(); it++) {
				output << (*it).first << "," << (*it).second << "\t";
			}
			output << "\n";
		}
		output.close();
		input.close();
		fs.close();
		delete myBPtree;

		break;
	case 'p':
		// print B+-Tree structure to [output file]
		myBPtree = new BTree(binFile);
		output.open(argv[3], ios::out);
		myBPtree->print(output);

		output.close();
		fs.close();
		delete myBPtree;

		break;
	}
	return 0;
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
