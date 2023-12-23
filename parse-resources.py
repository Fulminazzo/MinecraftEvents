#!/usr/bin/env python3
"""
	A script to merge together all
	the resources of subprojects to the
	root project.
"""
import yaml
import os

def get_folders(path: str) -> list:
	contents = os.listdir(path)
	return [f for f in contents if os.path.isdir(os.path.join(path, f)) and not f.startswith(".")]

def get_project_folders(path: str) -> list:
	contents = get_folders(path)
	for f in ["gradle", "build"]:
		contents.remove(f)
	return contents

def get_yaml_resources(path: str) -> list:
	resources = []
	for f in os.listdir(os.path.join(path, "src/main/resources")):
		if f.endswith(".yml"): resources.append(f)
	return resources

def parse_yaml(contents1: dict, contents2: dict):
	for (k, v) in contents2.items():
		if not k in contents1:
			contents1[k] = v
		else:
			prev = contents1[k]
			if isinstance(prev, Dict):
				parse_yaml(contents1[k], contents2[k])


def parse_resource(resource: str, modules: list):
	main_file = "src/main/resources/" + resource
	if not os.path.isfile(main_file):
		file = open(main_file, "r")
		file.close()

	with open(main_file, "r") as file
		contents = yaml.safeload(file)

	for module in modules:
		module_resource = module + "/" + main_file
		if not os.path.isfile(module_resource): continue
		with open(, "r") as tmp
			contents1 = yaml.safeload(tmp)
		parse_yaml(contents, contents1)

	yaml.dump(contents, main_file)

def main():
	cwd = os.getcwd()
	project = get_project_folders(cwd)
	if len(project) == 0:
		print("Not Java project or could not find folders.")
		print("Are you in the root directory?")
		return
	if len(project) == 1:
		print("Not multi-module project!")
		return
	if not "src" in project:
		print("Could not find src folder.")
		print("Are you in the root directory?")
		return

	modules = [f for f in project if not f == "src" and "src" in os.listdir(os.path.join(cwd, f))]

	full_modules = set()
	for module in ["", *modules]:
		for r in get_yaml_resources(module):
			full_modules.add(r)


if __name__ == '__main__':
	main()