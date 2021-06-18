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

struct entity {
	int key;
	int ID;
};//node entity

struct fileheader {
	int BlockSize;
	int RootBID;
	int Depth;
}; //BPtree header

bool compare(entity e1, entity e2) {
	return e1.key < e2.key;
}

class Node {
public:
    bool IsleafNode;//true=> LeafNode, false => nonleafnode
	entity* entities;
	int BID;// if LeafNode => NextLeafNode's BID if NoneLeafNode => Node that has smaller key than smallest key in Noneleafnode
    int size;
    int BlockID;//BID of this block
    Node(fstream& fs, int _BID, int hSize, int blockSize, bool _isleaf) {
        
        BlockID = _BID;
		size = (blockSize / 4) / 2;
		entities = new entity[size];
        fs.seekg(hSize + (_BID - 1) * blockSize);
		if (_isleaf == false) {
			fs.read((char*)&BID, 4);
		}
        for (int count = 0; count < size; count++) {
            fs.read((char*)&entities[count].ID, 4);
            fs.read((char*)&entities[count].key, 4);
        }
		if (_isleaf == true) {
			fs.read((char*)&BID, 4);
		}
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
	vector<int> trace;// 노드따라갈시 밟은 경로 노드 저장
    int headerSize; // header의 크기
    fileheader header; // Btree header
    fstream fs; // 파일입출력
    int Nodecount; // 트리안의 노드의 총 갯수
	int EntrySize; // Node당 entry 갯수
	
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
		if (node->IsleafNode == false) {
			fs.write((char*)&(node->BID), 4);
		}
        for (i = 0; i < node->size; i++) {
            fs.write((char*)&node->entities[i].key, 4);
            fs.write((char*)&node->entities[i].ID, 4);
        }
		if (node->IsleafNode == true) {
			fs.write((char*)&(node->BID), 4);
		}
        fs.write((char*)&node->BID, i + 1);
            
    }

    BTree(const char* filename) {
        fs.open(filename, ios::in | ios::out | ios::binary);
    };
    BTree(const char* filename, int _Bsize) {
		//generate file
		ofstream os (filename, ios::out | ios::binary);
		if (os.fail()) {
			cerr << "The file \"" << filename << "\" creation fail." << endl;
			exit(1);
		}

		os.close();

		//open file to read and write
        fs.open(filename, ios::in | ios::out | ios::binary);
		
		if (fs.fail()) {
			cerr << "The file \"" << filename << "\" open failed." << endl;
			exit(1);
		}

        header.RootBID = 0;
        header.BlockSize = _Bsize;
        header.Depth = 0;
        Nodecount = 0;
        headerSize = 12;
		EntrySize = (header.BlockSize / 4) / 2;

        write_header(header.BlockSize, header.RootBID, header.Depth);
    };

	//initialize header & trace 
    void init() {
        fs.seekg(0);
        fs.read((char*)&header.BlockSize, 4);
        fs.read((char*)&header.RootBID, 4);
        fs.read((char*)&header.Depth, 4);
        headerSize = 12;
		EntrySize = (header.BlockSize / 4) / 2;
		
    }

	
	//find Node that key can fit in 
	Node* Node_search(int _key) {
		trace.clear();
		Node* Noneleaf;
		Node* Leaf;

		int cur_node = header.RootBID;
		int height = header.Depth;
		trace.push_back(cur_node);


		//Empty Tree
		if (header.RootBID == 0) {
			return NULL;
		}
		//Not an Empty Tree, leaf Node's height = 0.
		while (height > 1) {
			Noneleaf = new Node(fs, cur_node, headerSize, header.BlockSize, false);
			for (int i = 0; i < EntrySize - 1; i++) {
				//Bigger than biggest
				if (Noneleaf->entities[i].key == 0) {
					cur_node = Noneleaf->entities[i].ID;
					trace.push_back(cur_node);
					break;
				}
				//  target_key < key 
				if (_key < Noneleaf->entities[i].key) {
					if (i == 0) {
						cur_node = Noneleaf->BID;
						trace.push_back(cur_node);
						break;
					}
					else {
						cur_node = Noneleaf->entities[i-1].ID;
						trace.push_back(cur_node);
						break;
					}
				}
				//target_key = key
				if (_key = Noneleaf->entities[i].key) {
					cur_node = Noneleaf->entities[i].ID;
					trace.push_back(cur_node);
					break;
				}
				height--;
			}
		}
		Leaf = new Node(fs, cur_node, headerSize, header.BlockSize, true);
		return Leaf;
	}
	// LN = leafNode


	
	bool insert(int key, int ID) {
		trace.clear();
		init();
		fs.seekg(-1, ios::end);

		//Empty tree
		if (header.RootBID == 0) {
			header.RootBID = 1;
			header.Depth++;
			write_header(header.BlockSize, header.RootBID, header.Depth);
			for (int count = 0; count < EntrySize; count++) {
				fs.write((char*)&key, 4);
				fs.write((char*)&ID, 4);
				key = 0;
				ID = 0;
			}
			fs.write("\0", 4);
		}
		// Not an Empty tree
		else {
			Node* target = Node_search(key);//go until leafNode
			int index;

			//find empty slot in Node
			for (index = 0; index < EntrySize; index++) {
				if (target->entities[index].key == 0) {
					break;
				}
			}
			target->entities[index].ID = ID;
			target->entities[index].key = key;
			sort(target->entities, target->entities + index + 1, compare);

			//if leaf node is full
			if (index == EntrySize) {

				//split
				Node* NewNode = new Node(header.BlockSize, ++Nodecount, true);
				int MovingEntries = (EntrySize + 1) / 2;
				NewNode->entities = new entity[EntrySize];
				for (int i = 0; i < MovingEntries; i++) {
					NewNode->entities[i] = target->entities[(EntrySize / 2 + 1) + index];
					NewNode->entities[(EntrySize / 2 + 1) + index].ID = 0;
					NewNode->entities[(EntrySize / 2 + 1) + index].key = 0;
				}
				NewNode->BID = target->BID;
				target->BID = NewNode->BlockID;
				trace.pop_back();
				if (!trace.empty()) {
					Node* temp = new Node(fs, trace.back(), headerSize, header.BlockSize, false); // Get ParentNode
					for (int i = 0; i < EntrySize; i++) {
						if (temp->entities[i].key == 0) {
							break;
						}
						if (NewNode->entities[0].key < temp->entities[i].key) {
							break;
						}
					}
				}
				write_node(target, headerSize);
				write_node(NewNode, headerSize);

				//root 뿐인 트리일경우
				if (header.Depth == 1) {
					Node* NLN = new Node(header.BlockSize, ++Nodecount, false);
					NLN->entities = new entity[EntrySize];
					NLN->BID = target->BlockID;
					NLN->entities[0].key = NewNode->entities[0].key;
					NLN->entities[0].ID = NewNode->BlockID;
					header.RootBID = NLN->BlockID;
					header.Depth++;
					write_node(NLN, headerSize);

				}
				else {
					Node* NLN = new Node(fs, trace.back(), headerSize, header.BlockSize, false);

					int i;
					for (i = 0; i < EntrySize; i++) {
						if (NLN->entities[i].key == 0) {
							NLN->entities[i].key = NewNode->entities[0].key;
							NLN->entities[i].ID = NewNode->BlockID;
							write_node(NLN, headerSize);
							break;
						}
					}
					if (i == EntrySize) {
						NLN->entities[i].key = NewNode->entities[0].key;
						NLN->entities[i].ID = NewNode->BlockID;
					}
					sort(NLN->entities, NLN->entities + i + 1, compare);

					//부모노드가 꽉찬 경우
					if (i == EntrySize) {
						Node* Extra = new Node(header.BlockSize, 0, false);
						Node* NN = new Node(header.BlockSize, 0, false);
						entity temp;
						temp.ID = 0;
						temp.key = 0;
						while (true) {
							if (temp.key != 0) {
								trace.pop_back();
								if (trace.empty()) {
									Node* LN = new Node(fs, NLN->entities[EntrySize / 2 - 1].ID, headerSize, header.BlockSize, true);
									LN->BID = NN->BID;
									write_node(LN, headerSize);
									Node* ancester = new Node(header.BlockSize, ++Nodecount, false);
									ancester->BID = Extra->BlockID;
									ancester->entities[0] = temp;
									header.Depth++;
									header.RootBID = ancester->BlockID;
									write_node(ancester, headerSize);
									break;
								}
								else {
									NN = new Node(fs, trace.back(), headerSize, header.BlockSize, false);
									i = 0;
									for (; i < NN->size; i++) {
										if (NN->entities[i].key == 0) {
											NN->entities[i] = temp;
											break;
										}
									}
									sort(NN->entities, NN->entities + i, compare);
									if (i < EntrySize) {
										write_node(NN, headerSize);
										break;
									}
								}
							}
							NN = new Node(header.BlockSize, ++Nodecount, false);
							NN->entities = new entity[EntrySize];
							int m = (EntrySize - 1) / 2;
							temp = NLN->entities[EntrySize / 2];
							NLN->entities[EntrySize / 2].ID = 0;
							NLN->entities[EntrySize / 2].key = 0;
							for (i = 0; i <= m; i++) {
								NN->entities[i] = NLN->entities[EntrySize / 2 + i + 1];
								NLN->entities[EntrySize / 2 + i + 1].key = 0;
								NLN->entities[EntrySize / 2 + i + 1].key = 0;
							}
							NN->BID = temp.ID;
							temp.ID = NN->BID;
							write_node(NLN, headerSize);
							write_node(NN, headerSize);
							Extra = NLN;
						}
					}
					else {
						write_node(NLN, headerSize);
					}
				}
				write_header(header.BlockSize, header.RootBID, header.Depth);
			}
			else {
				write_node(target, headerSize);
			}
		}
		return true;

	}
	
	//BFS
	void print(fstream& os) {
		init();
		bool* visit = new bool[Nodecount] {false};
		queue<int> q;
		q.push(header.RootBID);
		int cur = 0; // 0부터 시작하는 트리노드
		os << "\n<0>\n";
		Node* x = new Node(fs, q.front(), headerSize, header.BlockSize, false);
		q.pop();
		printNode(os, x);	
		for (int i = 0; i < EntrySize; i++) {
			if (x->entities[i].key == 0) {
				break;
			}
			if (!visit[cur]) {
				q.push(x->entities[i].ID);
				cur++;
			}				
		}
		os << "\n<1>\n";
		while (!q.empty()) {
			x = new Node(fs, q.front(), headerSize, header.BlockSize, false);
			q.pop();
			printNode(os, x);
		}
	}

	void printNode(fstream& os, Node* n) {
		for (int i = 0; i < EntrySize; i++) {
			if (n->entities[i].key == 0) break;
			os << n->entities[i].key << ",";
		}
	}

	int search(int key) {
		Node* Leaf = Node_search(key);
		for (int i = 0; i < Leaf->size; i++) {
			if (Leaf->entities[i].key == key) {
				return Leaf->entities[i].ID;
			}
		}
		return 0;
	}

	vector<entity> search(int start, int end) {
		vector<entity> val;
		int cur = start;
		Node* N = Node_search(start);
		for (int i= 0 ; cur <= end; i++) {
			if (i == EntrySize || N->entities[i].key == 0) {
				i = -1;
				N = new Node(fs, N->BID, headerSize, header.BlockSize , true);
				continue;
			}
			if (N->entities[i].key < cur) {
				continue;
			}
			cur = N->entities[i].key;
			if (cur <= end) {
				val.push_back(N->entities[i]);
			}
		}
		return val;
	}
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

	vector<entity> Vec;

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

			Vec = myBPtree->search(rangeStart, rangeEnd);
			for (vector<entity>::iterator it = Vec.begin(); it != Vec.end(); it++) {
				output << (*it).key << "," << (*it).ID << "\t";
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
